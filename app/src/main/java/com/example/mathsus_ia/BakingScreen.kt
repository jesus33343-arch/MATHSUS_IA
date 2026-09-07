package com.example.mathsus_ia

import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mathsus_ia.data.AiResponseReportSubmission
import com.example.mathsus_ia.data.AiResponseSatisfactionSubmission
import com.example.mathsus_ia.data.DeviceIdentity
import com.example.mathsus_ia.data.submitAiResponseReport
import com.example.mathsus_ia.data.submitAiResponseSatisfaction
import io.github.jesusgurrute.mathsus_ia.BuildConfig
import io.github.jesusgurrute.mathsus_ia.R
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import kotlinx.coroutines.launch

@Composable
fun BakingScreen(
    navController: NavController,
    bakingViewModel: BakingViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val deviceId = remember { DeviceIdentity.getOrCreateDeviceId(context) }
    val messages by bakingViewModel.messages.collectAsState()
    val isSending by bakingViewModel.isSending.collectAsState()
    var prompt by rememberSaveable { mutableStateOf("") }
    var reportingMessageId by rememberSaveable { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    val latexTextSizePx = with(LocalDensity.current) { 16.sp.toPx() }
    val markwon = remember(context) {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(
                JLatexMathPlugin.create(latexTextSizePx) { builder ->
                    builder.inlinesEnabled(true)
                }
            )
            .build()
    }

    LaunchedEffect(deviceId) {
        bakingViewModel.loadHistoryIfNeeded(deviceId)
    }

    LaunchedEffect(messages.size, isSending) {
        if (messages.isNotEmpty() || isSending) listState.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            IconButton(
                onClick = { navController.navigate("splash") },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = stringResource(R.string.ia_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Text(
            text = "Respuestas generadas por IA (Gemini). Pueden contener errores; verifica los resultados importantes.",
            style = MaterialTheme.typography.labelSmall,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(
            state = listState,
            reverseLayout = true,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isSending) {
                item { TypingBubble() }
            }

            val reversedMessages = messages.asReversed()
            itemsIndexed(reversedMessages, key = { _, message -> message.id }) { reversedIndex, message ->
                val index = messages.size - 1 - reversedIndex
                val relatedPrompt = if (
                    message.role == ChatRole.ASSISTANT &&
                    index > 0 &&
                    messages[index - 1].role == ChatRole.USER
                ) {
                    messages[index - 1].text
                } else {
                    null
                }

                ChatBubble(
                    message = message,
                    markwon = markwon,
                    isReporting = reportingMessageId == message.id,
                    onRate = if (message.aiQueryId != null && !message.isError) {
                        { satisfied ->
                            scope.launch {
                                runCatching {
                                    submitAiResponseSatisfaction(
                                        AiResponseSatisfactionSubmission(
                                            aiQueryId = message.aiQueryId,
                                            deviceId = deviceId,
                                            satisfied = satisfied
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        null
                    },
                    onReportComment = if (relatedPrompt != null && !message.isError) {
                        { comment ->
                            reportingMessageId = message.id
                            scope.launch {
                                try {
                                    submitAiResponseReport(
                                        AiResponseReportSubmission(
                                            prompt = relatedPrompt,
                                            response = message.text,
                                            reason = comment.ifBlank { null },
                                            appVersion = BuildConfig.VERSION_NAME
                                        )
                                    )
                                    Toast.makeText(context, "Gracias, lo revisaremos", Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "No se pudo enviar el comentario: ${e.localizedMessage ?: "revisa tu conexión"}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } finally {
                                    reportingMessageId = null
                                }
                            }
                        }
                    } else {
                        null
                    }
                )
            }

            if (messages.isEmpty() && !isSending) {
                item {
                    Text(
                        text = "Pregúntale a MATHSUS sobre Bisección, Regular Falsi, Newton-Raphson o Secante: pídele que te explique un paso, que te ayude a elegir un intervalo o valor inicial, o que verifique tu razonamiento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            TextField(
                value = prompt,
                onValueChange = { prompt = it },
                placeholder = { Text(stringResource(R.string.label_prompt)) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 5
            )

            IconButton(
                onClick = {
                    val toSend = prompt.trim()
                    prompt = ""
                    bakingViewModel.sendPrompt(toSend, deviceId)
                },
                enabled = prompt.isNotBlank() && !isSending,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.action_go),
                    tint = if (prompt.isNotBlank() && !isSending) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    markwon: Markwon,
    isReporting: Boolean,
    onRate: ((Boolean) -> Unit)?,
    onReportComment: ((String) -> Unit)?
) {
    val isUser = message.role == ChatRole.USER
    var satisfaction by remember(message.id) { mutableStateOf<Boolean?>(null) }
    var showCommentBox by remember(message.id) { mutableStateOf(false) }
    var comment by remember(message.id) { mutableStateOf("") }
    var commentSent by remember(message.id) { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            val bubbleColor = when {
                message.isError -> MaterialTheme.colorScheme.errorContainer
                isUser -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val contentColor = when {
                message.isError -> MaterialTheme.colorScheme.onErrorContainer
                isUser -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            val shape = if (isUser) {
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 4.dp)
            } else {
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
            }

            Surface(color = bubbleColor, shape = shape) {
                if (isUser) {
                    Text(
                        text = message.text,
                        color = contentColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                } else {
                    MarkdownLatexText(
                        markdown = message.text,
                        color = contentColor,
                        markwon = markwon,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }

            if (onRate != null) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                    TextButton(
                        onClick = {
                            satisfaction = true
                            showCommentBox = false
                            onRate(true)
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (satisfaction == true) "👍 ¡Gracias!" else "👍",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(
                        onClick = {
                            satisfaction = false
                            onRate(false)
                            if (onReportComment != null && !commentSent) showCommentBox = true
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "👎",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (showCommentBox && onReportComment != null && !commentSent) {
                    Text(
                        text = "¿En qué parte está el error? (un signo, una letra, un paso...)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    TextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = { Text("Cuéntanos qué está mal", style = MaterialTheme.typography.labelSmall) },
                        textStyle = MaterialTheme.typography.bodySmall,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                    TextButton(
                        onClick = {
                            onReportComment(comment.trim())
                            commentSent = true
                        },
                        enabled = !isReporting,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "Enviar", style = MaterialTheme.typography.labelSmall)
                    }
                }

                if (commentSent) {
                    Text(
                        text = "¡Gracias por el detalle, lo revisaremos!",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TypingBubble() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp)
        ) {
            Box(modifier = Modifier.padding(14.dp)) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Renders Markdown text with inline/block LaTeX (via Markwon + JLaTeXMath) using a
 * plain TextView, since Compose's Text() has no native math-rendering support.
 */
@Composable
private fun MarkdownLatexText(
    markdown: String,
    color: Color,
    markwon: Markwon,
    modifier: Modifier = Modifier
) {
    val colorArgb = color.toArgb()
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                textSize = 16f
                setTextColor(colorArgb)
            }
        },
        update = { textView ->
            textView.setTextColor(colorArgb)
            markwon.setMarkdown(textView, markdown)
        }
    )
}
