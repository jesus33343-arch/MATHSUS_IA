package com.example.mathsus_ia.ui.methods.secanteMethod

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathsus_ia.data.CalculationEventSubmission
import com.example.mathsus_ia.data.CalculationParams
import com.example.mathsus_ia.data.DeviceIdentity
import com.example.mathsus_ia.data.logCalculationEvent
import com.example.mathsus_ia.ui.methods.LatexText
import com.example.mathsus_ia.ui.methods.ResultFeedback
import com.example.mathsus_ia.ui.methods.functionExprToLatex
import com.example.mathsus_ia.ui.methods.rememberMathMarkwon
import io.github.jesusgurrute.mathsus_ia.BuildConfig
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.abs
import java.util.Locale

private val COLUMN_WIDTHS = listOf(40.dp, 110.dp, 110.dp, 90.dp)

private data class SecanteRow(
    val iteration: Int,
    val x: Double,
    val fx: Double,
    val error: Double
)

private data class SecanteRun(
    val rows: List<SecanteRow>,
    val root: Double,
    val iterations: Int,
    val errorMessage: String?
)

// Método de la secante: parte de DOS puntos iniciales x0, x1 (no de un intervalo [a,b] con
// cambio de signo como en Bisección). El caso inválido característico de este método es la
// división por cero cuando f(xn) = f(xn-1); también se protege la evaluación de f para no
// devolver una raíz falsa (0.0) cuando la expresión no se puede evaluar (p.ej. al caer en x = 0
// para funciones no definidas allí).
private fun runSecante(f: String, x0: Double, x1: Double, maxIterations: Int, epsilon: Double): SecanteRun {
    val computedRows = mutableListOf<SecanteRow>()

    var currentA = x0
    var currentB = x1
    var currentFa = evaluarFuncion(currentA, f)
    var currentFb = evaluarFuncion(currentB, f)

    if (currentFa.isNaN() || currentFb.isNaN()) {
        return SecanteRun(
            rows = emptyList(),
            root = 0.0,
            iterations = 0,
            errorMessage = "No se pudo evaluar f(x) = $f en x0 o x1. Verifique la expresión."
        )
    }

    // Intercambiar si es necesario al inicio, para partir del punto con menor |f(x)|
    if (abs(currentFa) > abs(currentFb)) {
        val tempA = currentA
        currentA = currentB
        currentB = tempA
        val tempFa = currentFa
        currentFa = currentFb
        currentFb = tempFa
    }

    var k = 0
    var previousX = 0.0
    val ea0 = if (currentA != 0.0) abs((currentA - previousX) / currentA) * 100 else 100.0
    computedRows.add(SecanteRow(k, currentA, currentFa, ea0))
    previousX = currentA

    var root = currentA
    var iterations = k

    for (i in 1..maxIterations) {
        if (abs(currentFa) < epsilon) {
            return SecanteRun(computedRows, currentA, k, null)
        }

        // Evitar división por cero cuando f(xn) == f(xn-1)
        val denominator = currentFb - currentFa
        if (abs(denominator) < 1e-12) {
            return SecanteRun(
                rows = computedRows,
                root = currentA,
                iterations = k,
                errorMessage = "No se pudo continuar: f(x$k) y f(x${k - 1}) son iguales, división por cero."
            )
        }

        var d = (currentB - currentA) / denominator
        currentB = currentA
        currentFb = currentFa
        d *= currentFa

        if (abs(d) < epsilon) {
            return SecanteRun(computedRows, currentA, k, null)
        }

        currentA -= d
        val nextFa = evaluarFuncion(currentA, f)
        if (nextFa.isNaN() || !currentA.isFinite()) {
            return SecanteRun(
                rows = computedRows,
                root = currentA,
                iterations = k,
                errorMessage = "No se pudo evaluar f(x) = $f en x = ${FormatNumber(currentA)}."
            )
        }
        currentFa = nextFa
        k++

        val ea = if (currentA != 0.0) abs((currentA - previousX) / currentA) * 100 else 100.0
        computedRows.add(SecanteRow(k, currentA, currentFa, ea))
        previousX = currentA

        root = currentA
        iterations = k
    }

    return SecanteRun(computedRows, root, iterations, null)
}

@SuppressLint("DefaultLocale")
@Composable
fun Secante(
    a: Double,
    b: Double,
    f: String,
    epsilon: Double,
    maxIterations: Int
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var calculationEventId by remember(f, a, b, epsilon, maxIterations) { mutableStateOf<String?>(null) }

    val run = remember(f, a, b, epsilon, maxIterations) {
        runSecante(f, a, b, maxIterations, epsilon)
    }

    Box(
        modifier = Modifier
            .width(380.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (run.errorMessage != null) {
                ErrorCard(message = run.errorMessage, colorScheme = colorScheme)
            } else {
                ResultSummaryCard(
                    functionExpr = f,
                    root = run.root,
                    iterations = run.iterations,
                    colorScheme = colorScheme
                )
            }

            if (run.rows.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(tableAsTsv(run.rows)))
                            Toast.makeText(context, "Tabla copiada al portapapeles", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "⧉ Copiar tabla")
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    TableHeader(colorScheme)
                    run.rows.forEach { row ->
                        TableRow(row, colorScheme)
                    }
                }
            }

            if (run.errorMessage == null) {
                LaunchedEffect(f, a, b, epsilon, maxIterations) {
                    calculationEventId = logCalculationEvent(
                        CalculationEventSubmission(
                            deviceId = DeviceIdentity.getOrCreateDeviceId(context),
                            method = "secante",
                            functionExpr = f,
                            params = CalculationParams(x0 = a, x1 = b, tolerance = epsilon, maxIterations = maxIterations),
                            rootValue = run.root,
                            iterations = run.iterations,
                            localeCountry = DeviceIdentity.localeCountry(),
                            timezone = DeviceIdentity.timezoneId(),
                            appVersion = BuildConfig.VERSION_NAME
                        )
                    )
                }

                ResultFeedback(calculationEventId)
            }
        }
    }
}

private fun tableAsTsv(rows: List<SecanteRow>): String {
    val header = "k\txk\tf(xk)\tea"
    val body = rows.joinToString("\n") { row ->
        "${row.iteration}\t${FormatNumber(row.x)}\t${FormatNumber(row.fx)}\t${FormatNumber(row.error)}"
    }
    return "$header\n$body"
}

@Composable
private fun ResultSummaryCard(
    functionExpr: String,
    root: Double,
    iterations: Int,
    colorScheme: ColorScheme
) {
    val markwon = rememberMathMarkwon()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Raíz encontrada",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(6.dp))
            LatexText(
                latex = "\$\$f(x) = ${functionExprToLatex(functionExpr)} \\; \\rightarrow \\; x \\approx ${FormatNumber(root)}\$\$",
                color = colorScheme.onPrimaryContainer,
                markwon = markwon,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 28.dp, max = 60.dp),
                textSizeSp = 16f
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$iterations iteraciones",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ErrorCard(message: String, colorScheme: ColorScheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.errorContainer)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            color = colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun TableHeader(colorScheme: ColorScheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.primary)
    ) {
        val labels = listOf("k", "xk", "f(xk)", "ea")
        labels.forEachIndexed { index, label ->
            Text(
                text = label,
                color = colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(COLUMN_WIDTHS[index])
                    .padding(vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun TableRow(row: SecanteRow, colorScheme: ColorScheme) {
    val rowBackground = if (row.iteration % 2 == 0) colorScheme.surface else colorScheme.surfaceVariant
    val rowTextColor = if (row.iteration % 2 == 0) colorScheme.onSurface else colorScheme.onSurfaceVariant

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(rowBackground)
        ) {
            val values = listOf(
                row.iteration.toString(),
                FormatNumber(row.x),
                FormatNumber(row.fx),
                FormatNumber(row.error)
            )
            values.forEachIndexed { index, value ->
                Text(
                    text = value,
                    color = rowTextColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier
                        .width(COLUMN_WIDTHS[index])
                        .padding(vertical = 8.dp, horizontal = 2.dp)
                )
            }
        }
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 0.5.dp)
    }
}

private const val SCIENTIFIC_UPPER_THRESHOLD = 100000.0
private const val SCIENTIFIC_LOWER_THRESHOLD = 0.0001

private val SUPERSCRIPT_DIGITS = mapOf(
    '0' to '⁰', '1' to '¹', '2' to '²', '3' to '³', '4' to '⁴',
    '5' to '⁵', '6' to '⁶', '7' to '⁷', '8' to '⁸', '9' to '⁹', '-' to '⁻'
)

private fun toSuperscript(exponent: Int): String =
    exponent.toString().map { SUPERSCRIPT_DIGITS[it] ?: it }.joinToString("")

// Decimal fijo para magnitudes normales; notación científica (con exponente en superíndice)
// solo para números muy grandes o muy pequeños, donde el decimal fijo perdería toda precisión
// o produciría una cadena demasiado larga para la celda. Siempre en Locale.US (punto decimal),
// sin importar el idioma configurado en el dispositivo.
fun FormatNumber(value: Double): String {
    if (value == 0.0) return "0.0000"
    val absValue = abs(value)
    return try {
        if (absValue >= SCIENTIFIC_UPPER_THRESHOLD || absValue < SCIENTIFIC_LOWER_THRESHOLD) {
            val formatted = String.format(Locale.US, "%.4e", value)
            val parts = formatted.split("e")
            val exponent = parts[1].toInt()
            val coefficient = if (parts[0].contains('.')) {
                parts[0].trimEnd('0').trimEnd('.')
            } else {
                parts[0]
            }
            "$coefficient×10${toSuperscript(exponent)}"
        } else {
            String.format(Locale.US, "%.4f", value)
        }
    } catch (e: Exception) {
        value.toString()
    }
}

// Función auxiliar no-@Composable para evaluar f(x) dentro del bucle de cálculo puro
// (runSecante). No puede usarse la versión @Composable `Metodo` aquí porque una función
// que invoca @Composable debe estar marcada como @Composable, y este helper se ejecuta
// fuera de composición. Señaliza los fallos de evaluación con NaN en vez de fingir f(x) = 0,
// para que runSecante pueda distinguir un fallo real de evaluación de una raíz genuina.
fun evaluarFuncion(x: Double, expresion: String): Double {
    return try {
        val f = org.mariuszgromada.math.mxparser.Function("f", expresion, "x")
        val expr = org.mariuszgromada.math.mxparser.Expression("f($x)", f)
        expr.calculate()
    } catch (e: Exception) {
        Double.NaN
    }
}
