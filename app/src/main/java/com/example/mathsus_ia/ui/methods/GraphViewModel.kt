package com.example.mathsus_ia.ui.methods

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function

import kotlin.math.min

class GraphViewModel : ViewModel() {
    var functionString by mutableStateOf("")
    var shouldShowGraph by mutableStateOf(false)
    var points by mutableStateOf<List<Pair<Float, Float>>>(emptyList())

    fun llamarGraphCoroutine(function: String) {
        viewModelScope.launch {
            functionString = function
            shouldShowGraph = true
            calculatePoints()
        }
    }

    suspend fun calculatePoints() {
        val mxFunction = Function("f", functionString, "x")
        points = withContext(Dispatchers.Default) {
            (-1000..1000 step 10).mapNotNull { x ->
                val xVal = x / 100f
                val yVal = calcularFuncionOptimized(xVal.toDouble(), mxFunction)
                if (yVal.isFinite()) Pair(xVal, yVal.toFloat()) else null
            }
        }
    }

    private fun calcularFuncionOptimized(a: Double, funcion: Function): Double {
        val expresion = Expression("f($a)", funcion)
        return expresion.calculate()
    }
}


@SuppressLint("AutoboxingStateCreation")
@Composable
fun FunctionGraph(
    viewModel: GraphViewModel,
    initialZoom: Float = 1f,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(initialZoom) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    LaunchedEffect(viewModel.functionString) {
        viewModel.calculatePoints()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .transformable(state = state)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            drawFunction(viewModel.points, scale, offset)
        }
    }
}

fun DrawScope.drawFunction(points: List<Pair<Float, Float>>, zoom: Float, offset: Offset) {
    val width = size.width
    val height = size.height
    val baseScale = min(width, height) / 10f

    withTransform({
        translate(offset.x, offset.y)
        scale(zoom, zoom, Offset(width / 2, height / 2))
    }) {
        // Dibujar ejes
        drawLine(Color.Black, Offset(0f, height / 2), Offset(width, height / 2), 2f)
        drawLine(Color.Black, Offset(width / 2, 0f), Offset(width / 2, height), 2f)

        // Dibujar números en los ejes
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 24f / zoom
        }

        // Dibujar números en el eje x
        for (i in -5..5) {
            val x = width / 2 + i * baseScale
            drawContext.canvas.nativeCanvas.drawText(
                i.toString(),
                x,
                height / 2 + 24f / zoom,
                textPaint
            )
        }

        // Dibujar números en el eje y
        for (i in -5..5) {
            val y = height / 2 - i * baseScale
            drawContext.canvas.nativeCanvas.drawText(
                (-i).toString(),
                width / 2 + 8f / zoom,
                y,
                textPaint
            )
        }

        // Dibujar la función

        if (points.isNotEmpty()) {
            for (i in 1 until points.size) {
                val (x1, y1) = points[i - 1]
                val (x2, y2) = points[i]
                drawLine(
                    Color.Blue,
                    Offset(width / 2 + x1 * baseScale, height / 2 - y1 * baseScale),
                    Offset(width / 2 + x2 * baseScale, height / 2 - y2 * baseScale),
                    2f
                )
            }
        }

    }

}


fun calcularFuncionOptimized(a: Double, funcion: Function): Double {
    val expresion = Expression("f($a)", funcion)
    return expresion.calculate()
}


/*
val mxFunction = Function("f", funcion, "x")
        clipRect(0f, 0f, width, height) {
            try {
                var lastX: Float? = null
                var lastY: Float? = null

                val step = (2000 / zoom).toInt().coerceAtLeast(1)

                // Usar withContext para mover los cálculos a un hilo de fondo
                val points = withContext(Dispatchers.Default) {
                    (-1000..1000 step step).map { x ->
                        val xVal = x / 100.0
                        val yVal = calcularFuncionOptimized(xVal, mxFunction)
                        Pair(xVal, yVal)
                    }
                }
                for ((xVal, yVal) in points) {
                    if (yVal.isFinite()) {
                        val currentX = width / 2 + xVal.toFloat() * baseScale
                        val currentY = height / 2 - yVal.toFloat() * baseScale

                        if (lastX != null && lastY != null) {
                            drawLine(
                                Color.Blue,
                                Offset(lastX, lastY),
                                Offset(currentX, currentY),
                                2f
                            )
                        }

                        lastX = currentX
                        lastY = currentY
                    } else {
                        lastX = null
                        lastY = null
                    }
                }
            } catch (e: Exception) {
                // Manejar error en caso de función inválida
                drawContext.canvas.nativeCanvas.drawText(
                    "Error: Función inválida",
                    width / 4,
                    height / 2,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 24f / zoom
                    }
                )
            }
        }
 */