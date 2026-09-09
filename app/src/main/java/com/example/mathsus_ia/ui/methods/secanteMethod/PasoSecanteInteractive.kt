package com.example.mathsus_ia.ui.methods.secanteMethod

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
import java.util.Locale
import kotlin.math.abs
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.mathsus_ia.ui.methods.LatexText
import com.example.mathsus_ia.ui.methods.rememberMathMarkwon

@Serializable
private data class SecantStep(
    val iteration: Int,
    val x0: Double,
    val x1: Double,
    val x2: Double,
    val fx0: Double,
    val fx1: Double,
    val fx2: Double,
    val errorPercent: Double?,
    val stopReason: String? = null
)

private fun secantNumber(value: Double): String = if (!value.isFinite()) "N/D" else String.format(Locale.US, "%.6g", value)

private fun latexSecantNumber(value: Double): String {
    if (!value.isFinite()) return "\\text{N/D}"
    if (value == 0.0) return "0"
    val magnitude = abs(value)
    if (magnitude >= 1e6 || magnitude < 1e-4) {
        val parts = String.format(Locale.US, "%.4e", value).split("e")
        return "${parts[0]}\\times 10^{${parts[1].toInt()}}"
    }
    return secantNumber(value)
}

@Composable
fun PasoBodySecanteInteractive(onAskAi: (String) -> Unit = {}) {
    val scheme = MaterialTheme.colorScheme
    val mathMarkwon = rememberMathMarkwon()
    var function by rememberSaveable { mutableStateOf("") }
    var x0Text by rememberSaveable { mutableStateOf("") }
    var x1Text by rememberSaveable { mutableStateOf("") }
    var toleranceText by rememberSaveable { mutableStateOf("") }
    var maxText by rememberSaveable { mutableStateOf("") }
    var currentX0 by rememberSaveable { mutableStateOf<Double?>(null) }
    var currentX1 by rememberSaveable { mutableStateOf<Double?>(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    var savedSteps by rememberSaveable { mutableStateOf("") }
    val steps = remember { mutableStateListOf<SecantStep>() }

    LaunchedEffect(Unit) {
        if (savedSteps.isNotBlank() && steps.isEmpty()) {
            runCatching { steps.addAll(Json.decodeFromString<List<SecantStep>>(savedSteps)) }
        }
    }

    fun reset(clearInputs: Boolean = false) {
        steps.clear(); savedSteps = ""; currentX0 = null; currentX1 = null; finished = false; message = null
        if (clearInputs) { function = ""; x0Text = ""; x1Text = ""; toleranceText = ""; maxText = "" }
    }

    fun validNumber(value: String) = value.replace(',', '.').matches(Regex("^-?\\d*\\.?\\d*$"))
    fun input(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier) {
        // This local helper is intentionally kept simple so decimal commas are normalized.
    }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Secante paso a paso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = scheme.primary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Observa cómo se construye cada nueva aproximación sin calcular toda la tabla de una vez.", color = scheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), textAlign = TextAlign.Center)
        OutlinedTextField(function, { function = it; reset() }, label = { Text("Función") }, placeholder = { Text("Ejemplo: x^2 - 2") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecantInput("x₀", x0Text, { x0Text = it; reset() }, Modifier.weight(1f), ::validNumber)
            SecantInput("x₁", x1Text, { x1Text = it; reset() }, Modifier.weight(1f), ::validNumber)
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecantInput("Tolerancia (%)", toleranceText, { toleranceText = it; reset() }, Modifier.weight(1f), ::validNumber)
            SecantInput("Máx. iteraciones", maxText, { maxText = it; reset() }, Modifier.weight(1f), ::validNumber, KeyboardType.Number)
        }
        Spacer(Modifier.height(10.dp))
        Surface(color = scheme.surfaceVariant, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
            Text("La Secante necesita dos valores iniciales distintos. Se detiene por error, raíz exacta o máximo de iteraciones; si f(xₙ)−f(xₙ₋₁) es casi cero, el paso no es seguro.", Modifier.padding(14.dp), color = scheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }

        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text("Historial: ${steps.size} iteración${if (steps.size == 1) "" else "es"}", color = scheme.primary, fontWeight = FontWeight.Bold)
            steps.forEach { step -> SecantStepCard(step, function, scheme, mathMarkwon, onAskAi) }
        }
        message?.let {
            Spacer(Modifier.height(10.dp))
            Surface(color = if (finished) scheme.primaryContainer else scheme.errorContainer, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (finished) Icons.Default.CheckCircle else Icons.Default.Refresh, null, tint = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer)
                    Spacer(Modifier.size(8.dp)); Text(it, color = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = !finished,
            onClick = {
                val x0Input = x0Text.replace(',', '.').toDoubleOrNull()
                val x1Input = x1Text.replace(',', '.').toDoubleOrNull()
                val tolerance = toleranceText.replace(',', '.').toDoubleOrNull()
                val limit = maxText.toIntOrNull()
                if (function.isBlank() || x0Input == null || x1Input == null || tolerance == null || limit == null || tolerance <= 0 || limit <= 0) { message = "Completa los campos con valores válidos. La tolerancia y el máximo deben ser mayores que cero."; return@Button }
                val x0 = currentX0 ?: x0Input
                val x1 = currentX1 ?: x1Input
                if (x0 == x1) { message = "x₀ y x₁ deben ser distintos."; return@Button }
                runCatching {
                    val fx0 = evaluarFuncion(x0.toString(), function)
                    val fx1 = evaluarFuncion(x1.toString(), function)
                    if (!fx0.isFinite() || !fx1.isFinite()) error("No se pudo evaluar la función en los puntos iniciales.")
                    val denominator = fx1 - fx0
                    if (abs(denominator) <= 1e-12) error("El denominador f(x₁) − f(x₀) es cero o demasiado pequeño.")
                    val x2 = x1 - fx1 * (x1 - x0) / denominator
                    if (!x2.isFinite()) error("La nueva aproximación no es finita; revisa los valores iniciales.")
                    val fx2 = evaluarFuncion(x2.toString(), function)
                    if (!fx2.isFinite()) error("No se pudo evaluar la función en la nueva aproximación.")
                    val errorPercent = if (x2 != 0.0) abs((x2 - x1) / x2) * 100.0 else abs(x2 - x1) * 100.0
                    val toleranceReached = abs(fx2) == 0.0 || errorPercent <= tolerance
                    val limitReached = steps.size + 1 >= limit
                    val reason = when {
                        abs(fx2) == 0.0 -> "Criterio de parada: f(x₂) = 0; se encontró una raíz exacta dentro de la precisión disponible."
                        toleranceReached -> "Criterio de parada: el error (${secantNumber(errorPercent)}%) alcanzó la tolerancia (${secantNumber(tolerance)}%)."
                        limitReached -> "Criterio de parada: se alcanzó el máximo de $limit iteraciones antes de llegar a la tolerancia."
                        else -> null
                    }
                    steps.add(SecantStep(steps.size + 1, x0, x1, x2, fx0, fx1, fx2, errorPercent, reason))
                    savedSteps = Json.encodeToString<List<SecantStep>>(steps.toList())
                    currentX0 = x1; currentX1 = x2
                    if (reason != null) { finished = true; message = reason }
                }.onFailure { message = it.message ?: "No se pudo calcular la iteración." }
            },
            modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.primary)
        ) { Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.size(8.dp)); Text(if (steps.isEmpty()) "Iniciar primera iteración" else "Calcular iteración ${steps.size + 1}", fontWeight = FontWeight.Bold) }
        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(8.dp)); Button(onClick = { reset() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.secondary)) { Icon(Icons.Default.Refresh, null); Spacer(Modifier.size(8.dp)); Text("Reiniciar proceso") }
        }
    }
}

@Composable
private fun SecantInput(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier, valid: (String) -> Boolean, keyboard: KeyboardType = KeyboardType.Decimal) {
    OutlinedTextField(value, { raw -> val normalized = raw.replace(',', '.'); if (normalized.isEmpty() || valid(normalized)) onChange(normalized) }, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = keyboard), modifier = modifier, shape = RoundedCornerShape(12.dp))
}

@Composable
private fun SecantStepCard(step: SecantStep, function: String, scheme: androidx.compose.material3.ColorScheme, mathMarkwon: io.noties.markwon.Markwon, onAskAi: (String) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = scheme.surfaceVariant), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Iteración ${step.iteration}", color = scheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("x₀ = ${secantNumber(step.x0)}   x₁ = ${secantNumber(step.x1)}", color = scheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                LatexText("\$\$x_2=x_1-\\frac{f(x_1)(x_1-x_0)}{f(x_1)-f(x_0)}=${latexSecantNumber(step.x2)}\$\$", scheme.onSurface, mathMarkwon, Modifier.fillMaxWidth().padding(12.dp), textSizeSp = 17f)
            }
            Row(Modifier.fillMaxWidth().border(1.dp, scheme.outline, RoundedCornerShape(10.dp)).background(scheme.surface, RoundedCornerShape(10.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("f(x₀)" to step.fx0, "f(x₁)" to step.fx1, "f(x₂)" to step.fx2).forEach { (label, value) -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) { Text(label, color = scheme.primary, fontWeight = FontWeight.Bold); Text(secantNumber(value), color = scheme.onSurface, fontFamily = FontFamily.Monospace, fontSize = 12.sp) } }
            }
            Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                LatexText("\$\$e_a=\\frac{|x_2-x_1|}{|x_2|}\\times100\\%= ${latexSecantNumber(step.errorPercent ?: 0.0)}\\%\$\$", scheme.onSurface, mathMarkwon, Modifier.fillMaxWidth().padding(10.dp), textSizeSp = 16f)
            }
            Text("Error aproximado: ${secantNumber(step.errorPercent ?: 0.0)}%${if (step.errorPercent == null) " (no aplica)" else ""}", color = scheme.onSurfaceVariant, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 8.dp))
            step.stopReason?.let { Text(it, color = scheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)) }
            TextButton(onClick = { onAskAi("Estoy revisando la iteración ${step.iteration} del método de la Secante. Función: $function. x₀=${secantNumber(step.x0)}, x₁=${secantNumber(step.x1)}, x₂=${secantNumber(step.x2)}. f(x₀)=${secantNumber(step.fx0)}, f(x₁)=${secantNumber(step.fx1)}, f(x₂)=${secantNumber(step.fx2)}. Error aproximado=${secantNumber(step.errorPercent ?: 0.0)}%. Explícame si este paso es correcto y qué significa el resultado.") }) { Text("Preguntar a MATHSUS sobre este paso") }
        }
    }
}
