package com.example.mathsus_ia.ui.methods.bisectionMethod

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
import com.example.mathsus_ia.ui.methods.Metodo
import com.example.mathsus_ia.ui.methods.calcularFuncion
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.abs
import java.util.Locale
import kotlin.math.sign

private val COLUMN_WIDTHS = listOf(40.dp, 80.dp, 80.dp, 80.dp, 80.dp)

private data class BisectionRow(
    val iteration: Int,
    val xl: Double,
    val xu: Double,
    val xr: Double,
    val error: Double
)

private data class BisectionRun(
    val rows: List<BisectionRow>,
    val root: Double,
    val iterations: Int
)

private fun runBisection(f: String, a: Double, b: Double, maxIter: Int, epsilon: Double): BisectionRun {
    val computedRows = mutableListOf<BisectionRow>()
    var currentXl = a
    var currentXu = b
    var previousXr = 0.0
    var iter = 0
    var root = 0.0
    var iterations = 0

    while (true) {
        val currentXr = (currentXl + currentXu) / 2
        val fr = calcularFuncion(a = currentXr, f = f)
        val fl = calcularFuncion(a = currentXl, f = f)
        iter++

        val ea = if (currentXr != 0.0) {
            abs((currentXr - previousXr) / currentXr) * 100
        } else {
            100.0
        }

        computedRows.add(BisectionRow(iter, currentXl, currentXu, currentXr, ea))

        var done = false
        when {
            fr == 0.0 -> {
                root = currentXr
                iterations = iter
                done = true
            }
            sign(fl) * sign(fr) < 0 -> currentXu = currentXr
            else -> currentXl = currentXr
        }

        if (done || ea <= epsilon || iter >= maxIter) {
            root = currentXr
            iterations = iter
            break
        }

        previousXr = currentXr
    }

    return BisectionRun(computedRows, root, iterations)
}

@SuppressLint("DefaultLocale")
@Composable
fun Bisection(
    f: String,
    a: Double,
    b: Double,
    MaxIter: Int,
    epsilon: Double,
) {
    val colorScheme = MaterialTheme.colorScheme

    // Initial validation
    val fa = Metodo(a = a, f = f)
    val fb = Metodo(a = b, f = f)

    if (fa * fb >= 0) {
        ErrorCard(
            message = "La función debe tener signos opuestos en los puntos a y b para aplicar el método de Bisección.",
            colorScheme = colorScheme
        )
        return
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var calculationEventId by remember(f, a, b, MaxIter, epsilon) { mutableStateOf<String?>(null) }

    val run = remember(f, a, b, MaxIter, epsilon) {
        runBisection(f, a, b, MaxIter, epsilon)
    }

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

            LaunchedEffect(f, a, b, MaxIter, epsilon) {
                calculationEventId = logCalculationEvent(
                    CalculationEventSubmission(
                        deviceId = DeviceIdentity.getOrCreateDeviceId(context),
                        method = "bisection",
                        functionExpr = f,
                        params = CalculationParams(a = a, b = b, tolerance = epsilon, maxIterations = MaxIter),
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

private fun tableAsTsv(rows: List<BisectionRow>): String {
    val header = "n\ta\tb\tc\tea"
    val body = rows.joinToString("\n") { row ->
        "${row.iteration}\t${FormatNumber(row.xl)}\t${FormatNumber(row.xu)}\t${FormatNumber(row.xr)}\t${FormatNumber(row.error)}"
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
        val labels = listOf("n", "a", "b", "c", "ea")
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
private fun TableRow(row: BisectionRow, colorScheme: ColorScheme) {
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
                FormatNumber(row.xl),
                FormatNumber(row.xu),
                FormatNumber(row.xr),
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
