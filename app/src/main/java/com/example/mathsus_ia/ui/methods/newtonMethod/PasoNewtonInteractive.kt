package com.example.mathsus.ui.methods.newtonMethod

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathsus_ia.ui.methods.LatexText
import com.example.mathsus_ia.ui.methods.rememberMathMarkwon
import java.util.Locale
import kotlin.math.abs
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
private data class NewtonStep(
    val iteration: Int,
    val x: Double,
    val fx: Double,
    val dfx: Double,
    val nextX: Double,
    val errorPercent: Double?,
    val stopReason: String? = null
)

private fun newtonNumber(value: Double): String = if (!value.isFinite()) "N/D" else String.format(Locale.US, "%.6g", value)

private fun newtonLatexNumber(value: Double): String {
    if (!value.isFinite()) return "\\text{N/D}"
    if (value == 0.0) return "0"
    if (abs(value) >= 1e6 || abs(value) < 1e-4) {
        val parts = String.format(Locale.US, "%.4e", value).split("e")
        return "${parts[0]}\\times 10^{${parts[1].toInt()}}"
    }
    return newtonNumber(value)
}

@Composable
fun PasoBodyNewtonInteractive(onAskAi: (String) -> Unit = {}) {
    val scheme = MaterialTheme.colorScheme
    val markwon = rememberMathMarkwon()
    var function by rememberSaveable { mutableStateOf("") }
    var xText by rememberSaveable { mutableStateOf("") }
    var toleranceText by rememberSaveable { mutableStateOf("") }
    var maxText by rememberSaveable { mutableStateOf("") }
    var currentX by rememberSaveable { mutableStateOf<Double?>(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    var savedSteps by rememberSaveable { mutableStateOf("") }
    val steps = remember { mutableStateListOf<NewtonStep>() }

    LaunchedEffect(Unit) { if (savedSteps.isNotBlank() && steps.isEmpty()) runCatching { steps.addAll(Json.decodeFromString<List<NewtonStep>>(savedSteps)) } }
    fun validNumber(value: String) = value.replace(',', '.').matches(Regex("^-?\\d*\\.?\\d*$"))
    fun reset(clearInputs: Boolean = false) {
        steps.clear(); savedSteps = ""; currentX = null; finished = false; message = null
        if (clearInputs) { function = ""; xText = ""; toleranceText = ""; maxText = "" }
    }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Newton-Raphson paso a paso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = scheme.primary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Observa cómo la tangente genera cada nueva aproximación.", color = scheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), textAlign = TextAlign.Center)
        OutlinedTextField(function, { function = it; reset() }, label = { Text("Función f(x)") }, placeholder = { Text("Ejemplo: x^2 - 2") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NewtonInput("x₀", xText, { xText = it; reset() }, Modifier.weight(1f), ::validNumber)
            NewtonInput("Tolerancia (%)", toleranceText, { toleranceText = it; reset() }, Modifier.weight(1f), ::validNumber)
            NewtonInput("Máx. iteraciones", maxText, { maxText = it; reset() }, Modifier.weight(1f), ::validNumber, KeyboardType.Number)
        }
        Spacer(Modifier.height(10.dp))
        Surface(color = scheme.surfaceVariant, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
            Text("Newton-Raphson necesita una aproximación inicial y una derivada que no sea cero o casi cero. La tolerancia se expresa como porcentaje.", Modifier.padding(14.dp), color = scheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(16.dp)); Text("Historial: ${steps.size} iteración${if (steps.size == 1) "" else "es"}", color = scheme.primary, fontWeight = FontWeight.Bold)
            steps.forEach { NewtonStepCard(it, function, scheme, markwon, onAskAi) }
        }
        message?.let { Spacer(Modifier.height(10.dp)); Surface(color = if (finished) scheme.primaryContainer else scheme.errorContainer, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) { Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Icon(if (finished) Icons.Default.CheckCircle else Icons.Default.Refresh, null, tint = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer); Spacer(Modifier.size(8.dp)); Text(it, color = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer, fontWeight = FontWeight.SemiBold) } } }
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = !finished,
            onClick = {
                val xInput = xText.replace(',', '.').toDoubleOrNull(); val tolerance = toleranceText.replace(',', '.').toDoubleOrNull(); val limit = maxText.toIntOrNull()
                if (function.isBlank() || xInput == null || tolerance == null || limit == null || tolerance <= 0 || limit <= 0) { message = "Completa los campos con valores válidos. La tolerancia y el máximo deben ser mayores que cero."; return@Button }
                val x = currentX ?: xInput
                runCatching {
                    val fx = evaluarFuncion1(x, function); val dfx = calculateDerivative(function, x)
                    if (!fx.isFinite() || !dfx.isFinite()) error("La función o su derivada produjo un valor no finito.")
                    if (abs(dfx) <= 1e-12) error("La derivada es cero o demasiado pequeña en x=${newtonNumber(x)}; Newton no puede continuar con seguridad.")
                    val nextX = x - fx / dfx
                    if (!nextX.isFinite()) error("La nueva aproximación no es finita; el método puede estar divergiendo.")
                    val errorPercent = if (nextX != 0.0) abs((nextX - x) / nextX) * 100.0 else abs(nextX - x) * 100.0
                    val nextFx = evaluarFuncion1(nextX, function)
                    val toleranceReached = abs(nextFx) == 0.0 || errorPercent <= tolerance
                    val limitReached = steps.size + 1 >= limit
                    val reason = when {
                        abs(nextFx) == 0.0 -> "Criterio de parada: f(xₙ₊₁) = 0; se encontró una raíz exacta dentro de la precisión disponible."
                        toleranceReached -> "Criterio de parada: el error (${newtonNumber(errorPercent)}%) alcanzó la tolerancia (${newtonNumber(tolerance)}%)."
                        limitReached -> "Criterio de parada: se alcanzó el máximo de $limit iteraciones antes de llegar a la tolerancia."
                        else -> null
                    }
                    steps.add(NewtonStep(steps.size + 1, x, fx, dfx, nextX, errorPercent, reason))
                    savedSteps = Json.encodeToString<List<NewtonStep>>(steps.toList()); currentX = nextX
                    if (reason != null) { finished = true; message = reason }
                }.onFailure { message = it.message ?: "No se pudo calcular la iteración." }
            },
            modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.primary)
        ) { Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.size(8.dp)); Text(if (steps.isEmpty()) "Iniciar primera iteración" else "Calcular iteración ${steps.size + 1}", fontWeight = FontWeight.Bold) }
        if (steps.isNotEmpty()) { Spacer(Modifier.height(8.dp)); Button(onClick = { reset() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.secondary)) { Icon(Icons.Default.Refresh, null); Spacer(Modifier.size(8.dp)); Text("Reiniciar proceso") } }
    }
}

@Composable
private fun NewtonInput(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier, valid: (String) -> Boolean, keyboard: KeyboardType = KeyboardType.Decimal) {
    OutlinedTextField(value, { raw -> val normalized = raw.replace(',', '.'); if (normalized.isEmpty() || valid(normalized)) onChange(normalized) }, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = keyboard), modifier = modifier, shape = RoundedCornerShape(12.dp))
}

@Composable
private fun NewtonStepCard(step: NewtonStep, function: String, scheme: androidx.compose.material3.ColorScheme, markwon: io.noties.markwon.Markwon, onAskAi: (String) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = scheme.surfaceVariant), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Iteración ${step.iteration}", color = scheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("xₙ = ${newtonNumber(step.x)}", color = scheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            Row(Modifier.fillMaxWidth().border(1.dp, scheme.outline, RoundedCornerShape(10.dp)).background(scheme.surface, RoundedCornerShape(10.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("f(xₙ)" to step.fx, "f'(xₙ)" to step.dfx, "xₙ₊₁" to step.nextX).forEach { (label, value) -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) { Text(label, color = scheme.primary, fontWeight = FontWeight.Bold); Text(newtonNumber(value), color = scheme.onSurface, fontFamily = FontFamily.Monospace, fontSize = 12.sp) } }
            }
            Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) { LatexText("\$\$x_{n+1}=x_n-\\frac{f(x_n)}{f'(x_n)}=${newtonLatexNumber(step.nextX)}\$\$", scheme.onSurface, markwon, Modifier.fillMaxWidth().padding(12.dp), textSizeSp = 17f) }
            Text("Derivada usada: f'(xₙ) = ${newtonNumber(step.dfx)}", color = scheme.onSurfaceVariant)
            Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) { LatexText("\$\$e_a=\\frac{|x_{n+1}-x_n|}{|x_{n+1}|}\\times100\\%= ${newtonLatexNumber(step.errorPercent ?: 0.0)}\\%\$\$", scheme.onSurface, markwon, Modifier.fillMaxWidth().padding(10.dp), textSizeSp = 16f) }
            step.stopReason?.let { Text(it, color = scheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)) }
            TextButton(onClick = { onAskAi("Estoy revisando la iteración ${step.iteration} de Newton-Raphson. Función: $function. xₙ=${newtonNumber(step.x)}, f(xₙ)=${newtonNumber(step.fx)}, f'(xₙ)=${newtonNumber(step.dfx)}, xₙ₊₁=${newtonNumber(step.nextX)}, error aproximado=${newtonNumber(step.errorPercent ?: 0.0)}%. Explícame si este paso es correcto y qué significa la derivada en él.") }) { Text("Preguntar a MATHSUS sobre este paso") }
        }
    }
}
