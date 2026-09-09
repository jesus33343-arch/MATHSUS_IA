package com.example.mathsus_ia.ui.features

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.mathsus_ia.data.interpretDrawnFunction
import kotlinx.coroutines.launch

@Composable
fun DrawFunctionDialog(
    method: String,
    onDismiss: () -> Unit,
    onExpressionConfirmed: (String) -> Unit
) {
    var strokes by remember { mutableStateOf<List<List<Offset>>>(emptyList()) }
    var drawingSize by remember { mutableStateOf(IntSize.Zero) }
    var expression by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dibuja tu función") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Escribe una función como x² − 4. Luego revisa la expresión detectada antes de usarla.")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .onSizeChanged { drawingSize = it }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { start -> strokes = strokes + listOf(listOf(start)) },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val last = strokes.lastOrNull() ?: return@detectDragGestures
                                    val point = last.lastOrNull()?.plus(dragAmount) ?: change.position
                                    strokes = strokes.dropLast(1) + listOf(last + point)
                                }
                            )
                        }
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        strokes.forEach { stroke ->
                            stroke.zipWithNext().forEach { (start, end) ->
                                drawLine(Color.Black, start, end, strokeWidth = 7f, cap = StrokeCap.Round)
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { strokes = strokes.dropLast(1); error = null }, enabled = strokes.isNotEmpty()) { Text("Deshacer") }
                    TextButton(onClick = { strokes = emptyList(); expression = ""; error = null }) { Text("Limpiar") }
                    Button(
                        onClick = {
                            if (strokes.isEmpty() || drawingSize == IntSize.Zero) {
                                error = "Dibuja primero la función."
                                return@Button
                            }
                            loading = true
                            error = null
                            scope.launch {
                                val image = encodeStrokes(strokes, drawingSize)
                                val result = runCatching { interpretDrawnFunction(image, method) }.getOrNull()
                                expression = result?.expression.orEmpty()
                                error = result?.error ?: if (expression.isBlank()) "No pude interpretar el dibujo." else null
                                loading = false
                            }
                        },
                        enabled = !loading
                    ) { if (loading) CircularProgressIndicator() else Text("Interpretar") }
                }
                if (expression.isNotBlank()) {
                    OutlinedTextField(
                        value = expression,
                        onValueChange = { expression = it },
                        label = { Text("Expresión detectada") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                error?.let { Text(it, color = Color(0xFFB3261E)) }
            }
        },
        confirmButton = {
            Button(onClick = { onExpressionConfirmed(expression); onDismiss() }, enabled = expression.isNotBlank()) {
                Text("Usar expresión")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun encodeStrokes(strokes: List<List<Offset>>, size: IntSize): String {
    val width = 1000
    val height = 560
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = AndroidCanvas(bitmap)
    canvas.drawColor(android.graphics.Color.WHITE)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 7f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }
    val sx = width.toFloat() / size.width.coerceAtLeast(1)
    val sy = height.toFloat() / size.height.coerceAtLeast(1)
    strokes.forEach { stroke ->
        stroke.zipWithNext().forEach { (start, end) ->
            canvas.drawLine(start.x * sx, start.y * sy, end.x * sx, end.y * sy, paint)
        }
    }
    val stream = java.io.ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    bitmap.recycle()
    return "data:image/png;base64," + Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
}
