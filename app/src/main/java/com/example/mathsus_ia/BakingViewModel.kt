package com.example.mathsus_ia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathsus_ia.data.AiChatTurn
import com.example.mathsus_ia.data.AiQuerySubmission
import com.example.mathsus_ia.data.DeviceIdentity
import com.example.mathsus_ia.data.askMathsusAi
import com.example.mathsus_ia.data.fetchConversationHistory
import com.example.mathsus_ia.data.logAiQuery
import io.github.jesusgurrute.mathsus_ia.BuildConfig
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ChatRole { USER, ASSISTANT }

/**
 * Markwon's LaTeX plugin only recognizes "$$" as a delimiter, and it pairs
 * whichever two "$$" occurrences come next in the text — if anything desyncs
 * that pairing, everything between two unrelated "$$" gets typeset as one
 * formula (e.g. Spanish prose rendered in italic math font).
 *
 * The actual cause (confirmed by inspecting raw stored responses): Gemini
 * regularly splits its own opening "$$" across a line break — literally
 * "$" + "\n" + "$[a, b]$$" instead of "$$[a, b]$$". That lone unmatched "$"
 * before the newline is invisible to a "$$" scan, so pairing desyncs for
 * every formula after it in the same message. Collapsing "$" + whitespace +
 * "$" back into "$$" before pairing fixes this at the source. The stopword
 * check and trailing-stray cleanup below are just a secondary safety net for
 * whatever this doesn't catch.
 */
private val LATEX_PROSE_STOPWORDS = setOf(
    "donde", "el", "la", "los", "las", "un", "una", "unos", "unas", "de", "del",
    "y", "o", "que", "es", "son", "para", "por", "con", "sin", "sea", "esta",
    "este", "estos", "estas", "cuando", "porque", "como", "muy", "mas", "más",
    "tanto", "tal", "cada", "si", "no", "se", "su", "sus", "lo", "al", "en", "pero"
)

private fun sanitizeLatexDelimiters(markdown: String): String {
    val normalized = markdown.replace(Regex("\\$\\s+\\$"), "\\$\\$")

    val pairRegex = Regex("\\$\\$([\\s\\S]+?)\\$\\$")
    var result = pairRegex.replace(normalized) { match ->
        val content = match.groupValues[1]
        val stopwordHits = content
            .split(Regex("[\\s,;:]+"))
            .count { word -> word.trim('.', ',', ':', ';').lowercase() in LATEX_PROSE_STOPWORDS }
        if (stopwordHits >= 2) content else match.value
    }
    val remainingCount = Regex("\\$\\$").findAll(result).count()
    if (remainingCount % 2 != 0) {
        val lastIndex = result.lastIndexOf("$$")
        if (lastIndex >= 0) {
            result = result.removeRange(lastIndex, lastIndex + 2)
        }
    }
    return result
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: ChatRole,
    val text: String,
    val isError: Boolean = false,
    val aiQueryId: String? = null
)

/**
 * El modelo/proveedor de IA (Gemini vía OpenRouter) vive del lado del servidor,
 * en la función "mathsus-ai-chat" de InsForge — no hay ninguna API key de IA
 * embebida en la app. Si Google descontinúa el modelo o se quiere cambiar de
 * proveedor, solo hay que actualizar el secreto AI_MODEL_NAME (o el código de
 * la función) en InsForge; no hace falta una nueva versión de la app.
 */
class BakingViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private var historyLoadedFor: String? = null

    fun loadHistoryIfNeeded(deviceId: String) {
        if (historyLoadedFor == deviceId) return
        historyLoadedFor = deviceId

        viewModelScope.launch(Dispatchers.IO) {
            runCatching { fetchConversationHistory(deviceId) }
                .onSuccess { rows ->
                    if (rows.isEmpty()) return@onSuccess
                    val loaded = rows.flatMap { row ->
                        buildList {
                            add(ChatMessage(role = ChatRole.USER, text = row.prompt))
                            if (!row.response.isNullOrBlank()) {
                                add(
                                    ChatMessage(
                                        role = ChatRole.ASSISTANT,
                                        text = sanitizeLatexDelimiters(row.response),
                                        aiQueryId = row.id
                                    )
                                )
                            }
                        }
                    }
                    _messages.value = loaded + _messages.value
                }
        }
    }

    fun sendPrompt(prompt: String, deviceId: String) {
        if (prompt.isBlank() || _isSending.value) return

        val history = _messages.value.map {
            AiChatTurn(role = if (it.role == ChatRole.USER) "user" else "assistant", content = it.text)
        }
        _messages.value = _messages.value + ChatMessage(role = ChatRole.USER, text = prompt)
        _isSending.value = true

        viewModelScope.launch(Dispatchers.IO) {
            var responseText: String? = null
            var modelUsed: String? = null
            val assistantMsgId = UUID.randomUUID().toString()
            try {
                val result = askMathsusAi(prompt, history, deviceId)
                if (result.error != null || result.text.isNullOrBlank()) {
                    _messages.value = _messages.value + ChatMessage(
                        id = assistantMsgId,
                        role = ChatRole.ASSISTANT,
                        text = result.error ?: "No se pudo obtener una respuesta. Intenta de nuevo.",
                        isError = true
                    )
                } else {
                    responseText = result.text
                    modelUsed = result.model
                    _messages.value = _messages.value + ChatMessage(
                        id = assistantMsgId,
                        role = ChatRole.ASSISTANT,
                        text = sanitizeLatexDelimiters(result.text)
                    )
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    id = assistantMsgId,
                    role = ChatRole.ASSISTANT,
                    text = e.localizedMessage ?: "Error al conectar con el asistente de IA",
                    isError = true
                )
            } finally {
                _isSending.value = false
                // Registro best-effort: si falla, no afecta la respuesta ya mostrada al estudiante.
                runCatching {
                    val queryId = logAiQuery(
                        AiQuerySubmission(
                            deviceId = deviceId,
                            modelName = modelUsed ?: "unknown",
                            prompt = prompt,
                            response = responseText,
                            localeCountry = DeviceIdentity.localeCountry(),
                            timezone = DeviceIdentity.timezoneId(),
                            appVersion = BuildConfig.VERSION_NAME
                        )
                    )
                    if (responseText != null) {
                        _messages.value = _messages.value.map {
                            if (it.id == assistantMsgId) it.copy(aiQueryId = queryId) else it
                        }
                    }
                }
            }
        }
    }
}
