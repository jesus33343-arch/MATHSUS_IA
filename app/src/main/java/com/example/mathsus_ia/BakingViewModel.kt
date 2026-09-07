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

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: ChatRole,
    val text: String,
    val isError: Boolean = false
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
                                add(ChatMessage(role = ChatRole.ASSISTANT, text = row.response))
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
            try {
                val result = askMathsusAi(prompt, history)
                if (result.error != null || result.text.isNullOrBlank()) {
                    _messages.value = _messages.value + ChatMessage(
                        role = ChatRole.ASSISTANT,
                        text = result.error ?: "No se pudo obtener una respuesta. Intenta de nuevo.",
                        isError = true
                    )
                } else {
                    responseText = result.text
                    modelUsed = result.model
                    _messages.value = _messages.value + ChatMessage(role = ChatRole.ASSISTANT, text = result.text)
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    role = ChatRole.ASSISTANT,
                    text = e.localizedMessage ?: "Error al conectar con el asistente de IA",
                    isError = true
                )
            } finally {
                _isSending.value = false
                // Registro best-effort: si falla, no afecta la respuesta ya mostrada al estudiante.
                runCatching {
                    logAiQuery(
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
                }
            }
        }
    }
}
