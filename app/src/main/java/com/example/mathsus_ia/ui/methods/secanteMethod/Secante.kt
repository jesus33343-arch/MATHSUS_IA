package com.example.mathsus_ia.ui.methods.secanteMethod

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mathsus_ia.data.CalculationEventSubmission
import com.example.mathsus_ia.data.CalculationParams
import com.example.mathsus_ia.data.DeviceIdentity
import com.example.mathsus_ia.data.logCalculationEvent
import com.example.mathsus_ia.ui.methods.ResultFeedback
import io.github.jesusgurrute.mathsus_ia.BuildConfig
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.methods.Metodo
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.abs

@SuppressLint("DefaultLocale")
@Composable
fun Secante(
    a: Double,
    b: Double,
    f: String,
    epsilon: Double,
    maxIterations: Int
) {
    // Clase de datos para almacenar la información de cada iteración
    data class IterationData(
        val iteration: Int,
        val x: Double,
        val fx: Double
    )

    // Estado para almacenar los resultados
    val iterations = remember { mutableStateListOf<IterationData>() }
    var rootFound by remember { mutableStateOf(false) }
    var rootValue by remember { mutableStateOf(0.0) }
    var totalIterations by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var calculationEventId by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Calcular fa y fb usando la función Metodo dentro del contexto @Composable
    val fa = Metodo(a = a, f = f)
    val fb = Metodo(a = b, f = f)

    // Calcular los resultados
    LaunchedEffect(a, b, f, epsilon) {
        // Limpiar resultados anteriores
        iterations.clear()
        rootFound = false
        errorMessage = null
        calculationEventId = null

        var k = 0
        var currentA = a
        var currentB = b
        var currentFa = fa
        var currentFb = fb
        var d: Double

        // Intercambiar si es necesario al inicio
        if (abs(currentFa) > abs(currentFb)) {
            currentA = currentB.also { currentB = currentA }
            currentFa = currentFb.also { currentFb = currentFa }
        }

        // Agregar el primer punto
        iterations.add(IterationData(k, currentA, currentFa))

        for (i in 1..maxIterations) {
            // Verificar si ya encontramos la raíz
            if (abs(currentFa) < epsilon) {
                rootFound = true
                rootValue = currentA
                break
            }

            // Evitar división por cero cuando f(xk) == f(xk-1)
            val denominator = currentFb - currentFa
            if (abs(denominator) < 1e-12) {
                errorMessage = "No se puede continuar: f(x${k}) y f(x${k - 1}) son iguales (división por cero)."
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                break
            }

            // Método de la secante
            d = (currentB - currentA) / denominator
            currentB = currentA
            currentFb = currentFa
            d *= currentFa

            // Verificar convergencia
            if (abs(d) < epsilon) {
                rootFound = true
                rootValue = currentA
                break
            }

            // Calcular el nuevo punto
            currentA -= d
            // No podemos llamar a Metodo aquí, así que calculamos una aproximación
            // Este es un punto crítico y limitante - ver comentario abajo
            val nextFa = evaluarFuncion(currentA, f)
            if (nextFa.isNaN() || !currentA.isFinite()) {
                errorMessage = "No se pudo evaluar f(x) = $f en x = $currentA."
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                break
            }
            currentFa = nextFa
            k++

            // Agregar el resultado de esta iteración
            iterations.add(IterationData(k, currentA, currentFa))
        }

        totalIterations = k

        if (rootFound) {
            calculationEventId = logCalculationEvent(
                CalculationEventSubmission(
                    deviceId = DeviceIdentity.getOrCreateDeviceId(context),
                    method = "secante",
                    functionExpr = f,
                    params = CalculationParams(x0 = a, x1 = b, tolerance = epsilon, maxIterations = maxIterations),
                    rootValue = rootValue,
                    iterations = k,
                    localeCountry = DeviceIdentity.localeCountry(),
                    timezone = DeviceIdentity.timezoneId(),
                    appVersion = BuildConfig.VERSION_NAME
                )
            )
        }
    }

    // Función para formatear números en notación científica
    fun formatScientific(value: Double): String {
        if (!value.isFinite()) return "N/D"
        val formatted = String.format("%.4e", value)
        val parts = formatted.split("e")
        val coefficient = parts[0].toDouble()
        val exponent = parts[1].toInt()

        val coefficientStr = if (coefficient % 1 == 0.0) {
            coefficient.toInt().toString()
        } else {
            parts[0].replace(Regex("0*$"), "")
        }

        return "$coefficientStr × 10^$exponent"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Encabezado con información del método
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Método de la Secante",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Función: $f")
                    Text("Intervalo inicial: [$a, $b]")
                    Text("Tolerancia: $epsilon")
                }
            }

            // Tabla de iteraciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Encabezado de la tabla
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "k",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.5f)
                        )
                        Text(
                            text = "xk",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "f(xk)",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Filas de la tabla con los datos de las iteraciones
                    iterations.forEach { data ->
                        val roundedX = formatScientific(data.x)
                        val roundedFx = formatScientific(data.fx)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (data.iteration % 2 == 0)
                                        colorResource(id = R.color.grisunicauca).copy(alpha = 0.5f)
                                    else
                                        colorResource(id = R.color.grisunicauca).copy(alpha = 0.3f)
                                )
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "${data.iteration}",
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                text = roundedX,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = roundedFx,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Mensaje de error, si lo hubo
            errorMessage?.let { msg ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Resultado final
            if (rootFound) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resultado:",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("La función $f tiene raíz real en x = ${formatScientific(rootValue)}")
                        Text("Con error aproximado menor que $epsilon")
                        Text("Encontrado en $totalIterations iteraciones")
                    }
                }
                ResultFeedback(calculationEventId)
            }
        }
    }
}

// Función para evaluar la función dentro de LaunchedEffect
// NOTA: Esta es una función auxiliar que debe ser reemplazada con la implementación real
// que utiliza org.mariuszgromada.math.mxparser si es posible
fun evaluarFuncion(x: Double, expresion: String): Double {
    // Implementación simplificada para evaluar la función
    // Debes reemplazar esto con una alternativa a Metodo que no sea @Composable
    // Por ejemplo, usando directamente org.mariuszgromada.math.mxparser

    // Esta es una implementación básica como ejemplo:
    return try {
        val f = org.mariuszgromada.math.mxparser.Function("f", expresion, "x")
        val expr = org.mariuszgromada.math.mxparser.Expression("f($x)", f)
        expr.calculate()
    } catch (e: Exception) {
        // En caso de error, señalizar con NaN en vez de fingir f(x) = 0
        Double.NaN
    }
}
