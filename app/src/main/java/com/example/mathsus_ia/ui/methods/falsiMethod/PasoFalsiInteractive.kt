package com.example.mathsus.ui.methods.falsiMethod

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
import com.example.mathsus_ia.ui.methods.calcularFuncion
import com.example.mathsus_ia.ui.methods.rememberMathMarkwon
import java.util.Locale
import kotlin.math.abs
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
private data class FalsiStep(
    val iteration: Int,
    val a: Double,
    val b: Double,
    val c: Double,
    val fa: Double,
    val fb: Double,
    val fc: Double,
    val nextA: Double,
    val nextB: Double,
    val errorPercent: Double?,
    val stopReason: String? = null
)

private fun falsiNumber(value: Double): String = if (!value.isFinite()) "N/D" else String.format(Locale.US, "%.6g", value)

private fun falsiLatexNumber(value: Double): String {
    if (!value.isFinite()) return "\\text{N/D}"
    if (value == 0.0) return "0"
    if (abs(value) >= 1e6 || abs(value) < 1e-4) {
        val parts = String.format(Locale.US, "%.4e", value).split("e")
        return "${parts[0]}\\times 10^{${parts[1].toInt()}}"
    }
    return falsiNumber(value)
}

@Composable
fun PasoBodyFalsiInteractive(onAskAi: (String) -> Unit = {}) {
    val scheme = MaterialTheme.colorScheme
    val markwon = rememberMathMarkwon()
    var function by rememberSaveable { mutableStateOf("") }
    var aText by rememberSaveable { mutableStateOf("") }
    var bText by rememberSaveable { mutableStateOf("") }
    var toleranceText by rememberSaveable { mutableStateOf("") }
    var maxText by rememberSaveable { mutableStateOf("") }
    var currentA by rememberSaveable { mutableStateOf<Double?>(null) }
    var currentB by rememberSaveable { mutableStateOf<Double?>(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    var savedSteps by rememberSaveable { mutableStateOf("") }
    val steps = remember { mutableStateListOf<FalsiStep>() }

    LaunchedEffect(Unit) {
        if (savedSteps.isNotBlank() && steps.isEmpty()) runCatching { steps.addAll(Json.decodeFromString<List<FalsiStep>>(savedSteps)) }
    }

    fun reset(clearInputs: Boolean = false) {
        steps.clear(); savedSteps = ""; currentA = null; currentB = null; finished = false; message = null
        if (clearInputs) { function = ""; aText = ""; bText = ""; toleranceText = ""; maxText = "" }
    }
    fun validNumber(value: String) = value.replace(',', '.').matches(Regex("^-?\\d*\\.?\\d*$"))

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Regular Falsi paso a paso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = scheme.primary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Avanza una falsa posición por clic y conserva todas las aproximaciones para compararlas.", color = scheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), textAlign = TextAlign.Center)
        OutlinedTextField(function, { function = it; reset() }, label = { Text("Función") }, placeholder = { Text("Ejemplo: x^2 - 2") }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FalsiInput("a", aText, { aText = it; reset() }, Modifier.weight(1f), ::validNumber)
            FalsiInput("b", bText, { bText = it; reset() }, Modifier.weight(1f), ::validNumber)
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FalsiInput("Tolerancia (%)", toleranceText, { toleranceText = it; reset() }, Modifier.weight(1f), ::validNumber)
            FalsiInput("Máx. iteraciones", maxText, { maxText = it; reset() }, Modifier.weight(1f), ::validNumber, KeyboardType.Number)
        }
        Spacer(Modifier.height(10.dp))
        Surface(color = scheme.surfaceVariant, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
            Text("Regular Falsi necesita un intervalo [a,b] con cambio de signo. La nueva aproximación es la intersección de la recta entre (a,f(a)) y (b,f(b)) con el eje x.", Modifier.padding(14.dp), color = scheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(16.dp)); Text("Historial: ${steps.size} iteración${if (steps.size == 1) "" else "es"}", color = scheme.primary, fontWeight = FontWeight.Bold)
            steps.forEach { FalsiStepCard(it, function, scheme, markwon, onAskAi) }
        }
        message?.let {
            Spacer(Modifier.height(10.dp)); Surface(color = if (finished) scheme.primaryContainer else scheme.errorContainer, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) { Icon(if (finished) Icons.Default.CheckCircle else Icons.Default.Refresh, null, tint = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer); Spacer(Modifier.size(8.dp)); Text(it, color = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer, fontWeight = FontWeight.SemiBold) }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = !finished,
            onClick = {
                val aInput = aText.replace(',', '.').toDoubleOrNull(); val bInput = bText.replace(',', '.').toDoubleOrNull(); val tolerance = toleranceText.replace(',', '.').toDoubleOrNull(); val limit = maxText.toIntOrNull()
                if (function.isBlank() || aInput == null || bInput == null || tolerance == null || limit == null || tolerance <= 0 || limit <= 0) { message = "Completa los campos con valores válidos. La tolerancia y el máximo deben ser mayores que cero."; return@Button }
                val a = currentA ?: aInput; val b = currentB ?: bInput
                if (a >= b) { message = "El intervalo debe cumplir a < b."; return@Button }
                runCatching {
                    val fa = calcularFuncion(a, function); val fb = calcularFuncion(b, function)
                    if (!fa.isFinite() || !fb.isFinite()) error("No se pudo evaluar la función en los extremos.")
                    if (steps.isEmpty() && fa * fb > 0) error("El intervalo inicial no cambia de signo: f(a) · f(b) debe ser menor que cero.")
                    val denominator = fa - fb
                    if (abs(denominator) <= 1e-12) error("No se puede calcular la falsa posición: f(a) − f(b) es demasiado pequeño.")
                    val c = b - fb * (a - b) / denominator
                    if (!c.isFinite()) error("La nueva aproximación no es finita.")
                    val fc = calcularFuncion(c, function)
                    if (!fc.isFinite()) error("No se pudo evaluar la función en la nueva aproximación.")
                    val previous = steps.lastOrNull()?.c
                    val errorPercent = previous?.let { if (c != 0.0) abs((c - it) / c) * 100.0 else abs(c - it) * 100.0 }
                    val nextA: Double; val nextB: Double
                    if (fa * fc <= 0) { nextA = a; nextB = c } else { nextA = c; nextB = b }
                    val toleranceReached = abs(fc) == 0.0 || (errorPercent != null && errorPercent <= tolerance)
                    val limitReached = steps.size + 1 >= limit
                    val reason = when {
                        abs(fc) == 0.0 -> "Criterio de parada: f(c) = 0; se encontró una raíz exacta dentro de la precisión disponible."
                        toleranceReached -> "Criterio de parada: el error (${falsiNumber(errorPercent ?: 0.0)}%) alcanzó la tolerancia (${falsiNumber(tolerance)}%)."
                        limitReached -> "Criterio de parada: se alcanzó el máximo de $limit iteraciones antes de llegar a la tolerancia."
                        else -> null
                    }
                    steps.add(FalsiStep(steps.size + 1, a, b, c, fa, fb, fc, nextA, nextB, errorPercent, reason))
                    savedSteps = Json.encodeToString<List<FalsiStep>>(steps.toList()); currentA = nextA; currentB = nextB
                    if (reason != null) { finished = true; message = reason }
                }.onFailure { message = it.message ?: "No se pudo calcular la iteración." }
            },
            modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.primary)
        ) { Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.size(8.dp)); Text(if (steps.isEmpty()) "Iniciar primera iteración" else "Calcular iteración ${steps.size + 1}", fontWeight = FontWeight.Bold) }
        if (steps.isNotEmpty()) { Spacer(Modifier.height(8.dp)); Button(onClick = { reset() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.secondary)) { Icon(Icons.Default.Refresh, null); Spacer(Modifier.size(8.dp)); Text("Reiniciar proceso") } }
    }
}

@Composable
private fun FalsiInput(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier, valid: (String) -> Boolean, keyboard: KeyboardType = KeyboardType.Decimal) {
    OutlinedTextField(value, { raw -> val normalized = raw.replace(',', '.'); if (normalized.isEmpty() || valid(normalized)) onChange(normalized) }, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = keyboard), modifier = modifier, shape = RoundedCornerShape(12.dp))
}

@Composable
private fun FalsiStepCard(step: FalsiStep, function: String, scheme: androidx.compose.material3.ColorScheme, markwon: io.noties.markwon.Markwon, onAskAi: (String) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = scheme.surfaceVariant), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Iteración ${step.iteration}", color = scheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Intervalo usado: [${falsiNumber(step.a)}, ${falsiNumber(step.b)}]", color = scheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) { LatexText("\$\$c=b-\\frac{f(b)(a-b)}{f(a)-f(b)}=${falsiLatexNumber(step.c)}\$\$", scheme.onSurface, markwon, Modifier.fillMaxWidth().padding(12.dp), textSizeSp = 17f) }
            Row(Modifier.fillMaxWidth().border(1.dp, scheme.outline, RoundedCornerShape(10.dp)).background(scheme.surface, RoundedCornerShape(10.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("f(a)" to step.fa, "f(b)" to step.fb, "f(c)" to step.fc).forEach { (label, value) -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) { Text(label, color = scheme.primary, fontWeight = FontWeight.Bold); Text(falsiNumber(value), color = scheme.onSurface, fontFamily = FontFamily.Monospace, fontSize = 12.sp) } }
            }
            Text(if (step.fa * step.fc <= 0) "f(a) · f(c) ≤ 0 → la raíz queda en [a,c]." else "f(c) · f(b) < 0 → la raíz queda en [c,b].", color = scheme.onSurface, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))
            Text("Nuevo intervalo: [${falsiNumber(step.nextA)}, ${falsiNumber(step.nextB)}]", color = scheme.primary, fontWeight = FontWeight.Bold)
            Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                if (step.errorPercent == null) LatexText("\$\$e_a\\text{ no aplica en la primera iteración}\$\$", scheme.onSurface, markwon, Modifier.fillMaxWidth().padding(10.dp), textSizeSp = 16f)
                else LatexText("\$\$e_a=\\frac{|c_n-c_{n-1}|}{|c_n|}\\times100\\%= ${falsiLatexNumber(step.errorPercent)}\\%\$\$", scheme.onSurface, markwon, Modifier.fillMaxWidth().padding(10.dp), textSizeSp = 16f)
            }
            step.stopReason?.let { Text(it, color = scheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)) }
            TextButton(onClick = { onAskAi("Estoy revisando la iteración ${step.iteration} de Regular Falsi. Función: $function. Intervalo [a,b]=[${falsiNumber(step.a)}, ${falsiNumber(step.b)}], c=${falsiNumber(step.c)}. f(a)=${falsiNumber(step.fa)}, f(b)=${falsiNumber(step.fb)}, f(c)=${falsiNumber(step.fc)}. Error aproximado=${falsiNumber(step.errorPercent ?: 0.0)}%. Nuevo intervalo [${falsiNumber(step.nextA)}, ${falsiNumber(step.nextB)}]. Explícame si este paso es correcto y por qué se conserva ese intervalo.") }) { Text("Preguntar a MATHSUS sobre este paso") }
        }
    }
}
