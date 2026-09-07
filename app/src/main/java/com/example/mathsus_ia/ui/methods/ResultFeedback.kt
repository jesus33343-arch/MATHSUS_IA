package com.example.mathsus_ia.ui.methods

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mathsus_ia.data.CalculationSatisfactionSubmission
import com.example.mathsus_ia.data.DeviceIdentity
import com.example.mathsus_ia.data.ExerciseCommentSubmission
import com.example.mathsus_ia.data.submitCalculationSatisfaction
import com.example.mathsus_ia.data.submitExerciseComment
import kotlinx.coroutines.launch

/**
 * Optional, non-blocking 👍/👎 + comment box shown after a calculation result.
 * Nothing here is required — if the student ignores it, no event is sent.
 */
@Composable
fun ResultFeedback(calculationEventId: String?) {
    if (calculationEventId == null) return

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val deviceId = remember { DeviceIdentity.getOrCreateDeviceId(context) }

    var satisfaction by remember(calculationEventId) { mutableStateOf<Boolean?>(null) }
    var showCommentBox by remember(calculationEventId) { mutableStateOf(false) }
    var comment by remember(calculationEventId) { mutableStateOf("") }
    var commentSent by remember(calculationEventId) { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(text = "¿Te sirvió este resultado?", style = MaterialTheme.typography.labelMedium)
        Row(modifier = Modifier.padding(top = 4.dp)) {
            OutlinedButton(
                onClick = {
                    if (satisfaction != true) {
                        satisfaction = true
                        scope.launch {
                            runCatching {
                                submitCalculationSatisfaction(
                                    CalculationSatisfactionSubmission(calculationEventId, deviceId, true)
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) { Text(if (satisfaction == true) "👍 ¡Gracias!" else "👍") }

            OutlinedButton(
                onClick = {
                    if (satisfaction != false) {
                        satisfaction = false
                        showCommentBox = true
                        scope.launch {
                            runCatching {
                                submitCalculationSatisfaction(
                                    CalculationSatisfactionSubmission(calculationEventId, deviceId, false)
                                )
                            }
                        }
                    }
                }
            ) { Text("👎") }
        }

        if (!showCommentBox && satisfaction == null) {
            Text(
                text = "¿El modelo se equivocó en un ejercicio? Cuéntanos qué salió mal.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 4.dp)
            )
            OutlinedButton(onClick = { showCommentBox = true }, modifier = Modifier.padding(top = 4.dp)) {
                Text("Comentar este ejercicio")
            }
        }

        if (showCommentBox && !commentSent) {
            TextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("¿Qué está mal? (se guarda junto con la función y el resultado)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            Button(
                onClick = {
                    if (comment.isBlank()) {
                        Toast.makeText(context, "Escribe brevemente qué está mal", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        try {
                            submitExerciseComment(
                                ExerciseCommentSubmission(calculationEventId, deviceId, comment.trim())
                            )
                            commentSent = true
                            Toast.makeText(context, "¡Gracias! Lo revisaremos para la próxima versión", Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "No se pudo enviar el comentario: ${e.localizedMessage ?: "revisa tu conexión"}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Enviar comentario")
            }
        }

        if (commentSent) {
            Text(
                text = "Comentario enviado, ¡gracias por ayudarnos a mejorar!",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
