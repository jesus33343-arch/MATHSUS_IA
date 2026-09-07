package com.example.mathsus_ia.ui.features

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mathsus_ia.data.FeedbackSubmission
import com.example.mathsus_ia.data.submitFeedback
import io.github.jesusgurrute.mathsus_ia.BuildConfig
import kotlinx.coroutines.launch

private val CATEGORIES = listOf(
    "bug" to "Reportar un error",
    "sugerencia" to "Sugerencia de mejora",
    "general" to "Comentario general"
)

@Composable
fun FeedbackScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf("sugerencia") }
    var rating by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
            }
            Text(text = "Danos tu feedback", style = MaterialTheme.typography.titleLarge)
        }

        Text(
            text = "Tu opinión nos ayuda a mejorar MATHSUS en la próxima versión.",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(text = "¿Cómo calificarías la app?", style = MaterialTheme.typography.labelLarge)
        Row(modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)) {
            for (star in 1..5) {
                Text(
                    text = if (star <= rating) "★" else "☆",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { rating = star }
                )
            }
        }

        Text(text = "Tipo de feedback", style = MaterialTheme.typography.labelLarge)
        Row(modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)) {
            CATEGORIES.forEach { (value, label) ->
                FilterChip(
                    selected = selectedCategory == value,
                    onClick = { selectedCategory = value },
                    label = { Text(label) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Cuéntanos qué mejorarías (obligatorio)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = contactEmail,
            onValueChange = { contactEmail = it },
            label = { Text("Tu correo (opcional, por si quieres respuesta)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (isSubmitting) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        } else {
            Button(
                onClick = {
                    if (message.isBlank()) {
                        Toast.makeText(context, "Escribe tu comentario antes de enviar", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isSubmitting = true
                    scope.launch {
                        try {
                            submitFeedback(
                                FeedbackSubmission(
                                    rating = rating.takeIf { it > 0 },
                                    category = selectedCategory,
                                    message = message.trim(),
                                    contactEmail = contactEmail.trim().ifBlank { null },
                                    appVersion = BuildConfig.VERSION_NAME
                                )
                            )
                            Toast.makeText(context, "¡Gracias por tu feedback!", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "No se pudo enviar el feedback: ${e.localizedMessage ?: "revisa tu conexión"}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            isSubmitting = false
                        }
                    }
                }
            ) {
                Text("Enviar feedback")
            }
        }
    }
}
