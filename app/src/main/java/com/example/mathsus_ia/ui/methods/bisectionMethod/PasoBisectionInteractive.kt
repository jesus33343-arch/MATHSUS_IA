package com.example.mathsus.ui.methods.bisectionMethod

import android.widget.Toast
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.mathsus_ia.ui.methods.secanteMethod.evaluarFuncion
import com.example.mathsus_ia.ui.methods.secanteMethod.formatearValor
import kotlin.math.abs
import java.util.Locale
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
private data class BisectionStep(
    val iteration: Int,
    val a: Double,
    val b: Double,
    val midpoint: Double,
    val fa: Double,
    val fb: Double,
    val fm: Double,
    val errorPercent: Double?,
    val nextA: Double,
    val nextB: Double,
    val stopReason: String?
)

private fun displayNumber(value: Double): String {
    if (!value.isFinite()) return "N/D"
    return String.format(Locale.US, "%.6g", value)
}

@Composable
fun PasoBodyBisectionInteractive(onAskAi: (String) -> Unit = {}) {
    val scheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    var function by rememberSaveable { mutableStateOf("") }
    var initialA by rememberSaveable { mutableStateOf("") }
    var initialB by rememberSaveable { mutableStateOf("") }
    var tolerance by rememberSaveable { mutableStateOf("") }
    var maxIterations by rememberSaveable { mutableStateOf("") }
    var currentA by rememberSaveable { mutableStateOf<Double?>(null) }
    var currentB by rememberSaveable { mutableStateOf<Double?>(null) }
    var finished by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    var savedSteps by rememberSaveable { mutableStateOf("") }
    val steps = remember { mutableStateListOf<BisectionStep>() }
    LaunchedEffect(Unit) {
        if (savedSteps.isNotBlank() && steps.isEmpty()) {
            runCatching {
                steps.addAll(Json.decodeFromString<List<BisectionStep>>(savedSteps))
            }
        }
    }

    fun resetProcess(clearInputs: Boolean = false) {
        steps.clear()
        savedSteps = ""
        currentA = null
        currentB = null
        finished = false
        message = null
        if (clearInputs) {
            function = ""
            initialA = ""
            initialB = ""
            tolerance = ""
            maxIterations = ""
        }
    }

    fun validNumber(value: String) = value.replace(',', '.').matches(Regex("^-?\\d*\\.?\\d*$"))

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Bisección paso a paso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = scheme.primary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Avanza una iteración por clic y comprueba cada operación con tu cuaderno.", style = MaterialTheme.typography.bodyMedium, color = scheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp, bottom = 16.dp), textAlign = TextAlign.Center)

        StepInput("1. Función", "Ejemplo: x^2 - 2", function, { function = it; resetProcess() })
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NumericInput("a", initialA, { initialA = it; resetProcess() }, Modifier.weight(1f), ::validNumber)
            NumericInput("b", initialB, { initialB = it; resetProcess() }, Modifier.weight(1f), ::validNumber)
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NumericInput("Máx. iteraciones", maxIterations, { maxIterations = it; resetProcess() }, Modifier.weight(1f), ::validNumber, KeyboardType.Number)
            NumericInput("Tolerancia (%)", tolerance, { tolerance = it; resetProcess() }, Modifier.weight(1f), ::validNumber)
        }

        Spacer(Modifier.height(12.dp))
        Surface(color = scheme.surfaceVariant, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
            Text("La tolerancia se interpreta como porcentaje. En cada paso calculamos el error aproximado entre dos puntos medios consecutivos.", Modifier.padding(14.dp), color = scheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }

        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text("Progreso: ${steps.size} iteración${if (steps.size == 1) "" else "es"}", color = scheme.primary, fontWeight = FontWeight.Bold)
            steps.forEach { step ->
                BisectionStepCard(step, function, scheme) { prompt -> onAskAi(prompt) }
            }
        }

        message?.let {
            Spacer(Modifier.height(10.dp))
            Surface(color = if (finished) scheme.primaryContainer else scheme.errorContainer, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (finished) Icons.Default.CheckCircle else Icons.Default.Refresh, null, tint = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer)
                    Spacer(Modifier.size(8.dp))
                    Text(it, color = if (finished) scheme.onPrimaryContainer else scheme.onErrorContainer, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            enabled = !finished,
            onClick = {
                val a0 = initialA.replace(',', '.').toDoubleOrNull()
                val b0 = initialB.replace(',', '.').toDoubleOrNull()
                val tol = tolerance.replace(',', '.').toDoubleOrNull()
                val limit = maxIterations.toIntOrNull()
                if (function.isBlank() || a0 == null || b0 == null || tol == null || limit == null || tol <= 0 || limit <= 0) {
                    message = "Completa todos los campos con valores válidos. La tolerancia y el máximo deben ser mayores que cero."
                    return@Button
                }
                val a = currentA ?: a0
                val b = currentB ?: b0
                if (a >= b) { message = "El intervalo debe cumplir a < b."; return@Button }
                val fa = evaluarFuncion(a.toString(), function)
                val fb = evaluarFuncion(b.toString(), function)
                if (!fa.isFinite() || !fb.isFinite()) { message = "No se pudo evaluar la función en los extremos."; return@Button }
                if (steps.isEmpty() && fa * fb > 0) { message = "El intervalo inicial no cambia de signo: f(a) · f(b) debe ser menor o igual que cero."; return@Button }
                val midpoint = (a + b) / 2.0
                val fm = evaluarFuncion(midpoint.toString(), function)
                if (!fm.isFinite()) { message = "No se pudo evaluar la función en el punto medio."; return@Button }
                val previous = steps.lastOrNull()?.midpoint
                val error = previous?.let { if (midpoint != 0.0) abs((midpoint - it) / midpoint) * 100.0 else abs(midpoint - it) * 100.0 }
                val toleranceReached = abs(fm) == 0.0 || (error != null && error <= tol)
                val limitReached = steps.size + 1 >= limit
                val nextA: Double
                val nextB: Double
                if (fa * fm <= 0) { nextA = a; nextB = midpoint } else { nextA = midpoint; nextB = b }
                val reason = when {
                    abs(fm) == 0.0 -> "f(m) = 0: se encontró una raíz exacta dentro de la precisión disponible."
                    toleranceReached -> "Criterio de parada: el error aproximado (${displayNumber(error ?: 0.0)}%) alcanzó la tolerancia (${displayNumber(tol)}%)."
                    limitReached -> "Criterio de parada: se alcanzó el máximo de $limit iteraciones antes de llegar a la tolerancia."
                    else -> null
                }
                steps.add(BisectionStep(steps.size + 1, a, b, midpoint, fa, fb, fm, error, nextA, nextB, reason))
                savedSteps = Json.encodeToString<List<BisectionStep>>(steps.toList())
                currentA = nextA
                currentB = nextB
                if (reason != null) { finished = true; message = reason }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = scheme.primary)
        ) {
            Icon(Icons.Default.PlayArrow, null)
            Spacer(Modifier.size(8.dp))
            Text(if (steps.isEmpty()) "Iniciar primera iteración" else "Calcular iteración ${steps.size + 1}", fontWeight = FontWeight.Bold)
        }

        if (steps.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Button(onClick = { resetProcess() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = scheme.secondary)) {
                Icon(Icons.Default.Refresh, null); Spacer(Modifier.size(8.dp)); Text("Reiniciar proceso")
            }
        }
    }
}

@Composable
private fun StepInput(label: String, placeholder: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value, onChange, label = { Text(label) }, placeholder = { Text(placeholder) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
}

@Composable
private fun NumericInput(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier, valid: (String) -> Boolean, keyboardType: KeyboardType = KeyboardType.Decimal) {
    OutlinedTextField(value, { raw -> val v = raw.replace(',', '.'); if (v.isEmpty() || valid(v)) onChange(v) }, label = { Text(label) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = keyboardType), modifier = modifier, shape = RoundedCornerShape(12.dp))
}

@Composable
private fun BisectionStepCard(step: BisectionStep, function: String, scheme: androidx.compose.material3.ColorScheme, onAskAi: (String) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = scheme.surfaceVariant), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Iteración ${step.iteration}", color = scheme.primary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text("Intervalo usado: [${displayNumber(step.a)}, ${displayNumber(step.b)}]", color = scheme.onSurfaceVariant)
            FormulaLine("m${step.iteration} = (a + b) / 2 = ${displayNumber(step.midpoint)}", scheme)
            ValueGrid(listOf("f(a)" to step.fa, "f(b)" to step.fb, "f(m)" to step.fm), scheme)
            Spacer(Modifier.height(8.dp))
            Text(if (step.fa * step.fm <= 0) "f(a) · f(m) ≤ 0 → la raíz queda en [a, m]." else "f(m) · f(b) < 0 → la raíz queda en [m, b].", color = scheme.onSurface, fontWeight = FontWeight.Medium)
            Text("Nuevo intervalo: [${displayNumber(step.nextA)}, ${displayNumber(step.nextB)}]", color = scheme.primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Error aproximado: ${step.errorPercent?.let { displayNumber(it) + "%" } ?: "no aplica en la primera iteración"}", color = scheme.onSurfaceVariant, fontFamily = FontFamily.Monospace)
            step.stopReason?.let { Text(it, Modifier.padding(top = 8.dp), color = scheme.primary, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = {
                val error = step.errorPercent?.let { displayNumber(it) + "%" } ?: "no disponible (primera iteración)"
                onAskAi("Estoy revisando la iteración ${step.iteration} del método de Bisección. Función: $function. Intervalo usado: [${displayNumber(step.a)}, ${displayNumber(step.b)}]. Punto medio: ${displayNumber(step.midpoint)}. f(a)=${displayNumber(step.fa)}, f(b)=${displayNumber(step.fb)}, f(m)=${displayNumber(step.fm)}. Error aproximado: $error. Nuevo intervalo: [${displayNumber(step.nextA)}, ${displayNumber(step.nextB)}]. Explícame si este paso es correcto y por qué se elige ese intervalo.")
            }) {
                Text("Preguntar a MATHSUS sobre este paso")
            }
        }
    }
}

@Composable
private fun FormulaLine(text: String, scheme: androidx.compose.material3.ColorScheme) {
    Surface(color = scheme.surface, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) { Text(text, Modifier.padding(12.dp).fillMaxWidth(), textAlign = TextAlign.Center, fontFamily = FontFamily.Monospace, color = scheme.onSurface) }
}

@Composable
private fun ValueGrid(values: List<Pair<String, Double>>, scheme: androidx.compose.material3.ColorScheme) {
    Row(Modifier.fillMaxWidth().border(1.dp, scheme.outline, RoundedCornerShape(10.dp)).background(scheme.surface, RoundedCornerShape(10.dp)).padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        values.forEach { (label, value) -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) { Text(label, color = scheme.primary, fontWeight = FontWeight.Bold); Text(displayNumber(value), color = scheme.onSurface, fontFamily = FontFamily.Monospace, fontSize = 12.sp) } }
    }
}
