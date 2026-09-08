package com.example.mathsus.ui.methods.newtonMethod

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
import com.example.mathsus_ia.ui.methods.calcularDerivada
import com.example.mathsus_ia.ui.methods.calcularFuncion
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.abs
import java.util.Locale

private val COLUMN_WIDTHS = listOf(40.dp, 80.dp, 80.dp, 80.dp, 80.dp)

private const val MAX_ITERATIONS = 200
private const val MIN_DERIVATIVE = 1e-10

private enum class NewtonStatus { SUCCESS, DERIVATIVE_TOO_SMALL, NON_FINITE }

private data class NewtonRow(
    val iteration: Int,
    val x: Double,
    val fx: Double,
    val dfx: Double,
    val error: Double
)

private data class NewtonRun(
    val status: NewtonStatus,
    val rows: List<NewtonRow>,
    val root: Double,
    val iterations: Int,
    val failureX: Double
)

// Bucle de cálculo puro (no-Composable): evalúa f(x) y f'(x) con las variantes
// no-Composable de Support.kt para poder invocarse desde una función normal.
private fun runNewton(f: String, x0: Double, epsilon: Double): NewtonRun {
    val rows = mutableListOf<NewtonRow>()
    var xk = x0
    var iter = 0

    while (iter < MAX_ITERATIONS) {
        val fxk = calcularFuncion(a = xk, f = f)
        val dfxk = calcularDerivada(a = xk, f = f)
        iter++

        if (abs(dfxk) < MIN_DERIVATIVE) {
            return NewtonRun(NewtonStatus.DERIVATIVE_TOO_SMALL, rows, xk, iter, xk)
        }

        val d = fxk / dfxk
        val xkPlus1 = xk - d

        if (!fxk.isFinite() || !dfxk.isFinite() || !xkPlus1.isFinite()) {
            return NewtonRun(NewtonStatus.NON_FINITE, rows, xk, iter, xk)
        }

        rows.add(NewtonRow(iter, xk, fxk, dfxk, abs(d)))

        if (abs(d) < epsilon) {
            return NewtonRun(NewtonStatus.SUCCESS, rows, xkPlus1, iter, xk)
        }

        xk = xkPlus1
    }

    return NewtonRun(NewtonStatus.SUCCESS, rows, xk, iter, xk)
}

@SuppressLint("DefaultLocale")
@Composable
fun NewtonRaphson(
    x: Double,
    f: String,
    error: Double
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val run = remember(f, x, error) {
        runNewton(f, x, error)
    }

    if (run.status != NewtonStatus.SUCCESS) {
        val message = when (run.status) {
            NewtonStatus.DERIVATIVE_TOO_SMALL ->
                "La derivada se hizo demasiado pequeña cerca de x ≈ ${FormatNumber(run.failureX)}, no se puede continuar con el método de Newton-Raphson."
            NewtonStatus.NON_FINITE ->
                "El cálculo produjo un valor no finito (posible divergencia) cerca de x ≈ ${FormatNumber(run.failureX)}, no se puede continuar con el método de Newton-Raphson."
            else -> ""
        }
        ErrorCard(message = message, colorScheme = colorScheme)
        return
    }

    var calculationEventId by remember(f, x, error) { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .width(380.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ResultSummaryCard(
                functionExpr = f,
                root = run.root,
                iterations = run.iterations,
                colorScheme = colorScheme
            )

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

            LaunchedEffect(f, x, error) {
                calculationEventId = logCalculationEvent(
                    CalculationEventSubmission(
                        deviceId = DeviceIdentity.getOrCreateDeviceId(context),
                        method = "newton",
                        functionExpr = f,
                        params = CalculationParams(x0 = x, tolerance = error, maxIterations = MAX_ITERATIONS),
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

private fun tableAsTsv(rows: List<NewtonRow>): String {
    val header = "n\tx\tf(x)\tf'(x)\terror"
    val body = rows.joinToString("\n") { row ->
        "${row.iteration}\t${FormatNumber(row.x)}\t${FormatNumber(row.fx)}\t${FormatNumber(row.dfx)}\t${FormatNumber(row.error)}"
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
        val labels = listOf("n", "x", "f(x)", "f'(x)", "error")
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
private fun TableRow(row: NewtonRow, colorScheme: ColorScheme) {
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
                FormatNumber(row.dfx),
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
