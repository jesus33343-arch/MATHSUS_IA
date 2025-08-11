package com.example.mathsus_ia.ui.methods.secanteMethod

import android.annotation.SuppressLint
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mathsus.ui.features.nav_menu_secante.ResultadoSecante
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.methods.FunctionGraph
import com.example.mathsus_ia.ui.methods.GraphViewModel
import org.mariuszgromada.math.mxparser.Function
import kotlin.math.abs
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale



@Composable
fun BodySecante() {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel: GraphViewModel = viewModel()
    val showGraph = remember { mutableStateOf(false) }
    //val wallSecante = "https://kodular-community.s3.dualstack.eu-west-1.amazonaws.com/original/3X/c/e/ce82ada4d9e8591f01abefebfab0dba4a8228eee.png"

    val f = remember { mutableStateOf("") }
    val a = remember { mutableStateOf("") }
    val b = remember { mutableStateOf("") }
    val x2 = remember { mutableStateOf("") }
    val MaxIter = remember { mutableStateOf("") }
    val bandera = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp)
    ) {

        OutlinedTextField(
            label = { Text(text = "Ingrese la función") },
            value = f.value,
            onValueChange = {
                if (f.value.length <= 30)
                    f.value = it
            },
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            OutlinedTextField(
                label = { Text(text = "a") },
                value = a.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        a.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            )

            OutlinedTextField(
                label = { Text(text = "b") },
                value = b.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        b.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            )

            OutlinedTextField(
                label = { Text(text = "tol") },
                value = x2.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        x2.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            )

            OutlinedTextField(
                label = { Text(text = "iter") },
                value = MaxIter.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        MaxIter.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Button(
                onClick = {
                    if (a.value.isEmpty() || b.value.isEmpty() || x2.value.isEmpty() || f.value.isEmpty() || MaxIter.value.isEmpty()) {
                        Toast.makeText(context, "No deje datos vacios", Toast.LENGTH_SHORT).show()
                    } else {
                        bandera.value = a.value
                        Toast.makeText(context, "Calculando", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
            ) {
                Text(text = "Calcular")
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Nuevo botón para limpiar los campos
            Button(
                onClick = {
                    f.value = ""
                    a.value = ""
                    b.value = ""
                    x2.value = ""
                    bandera.value = ""
                    MaxIter.value = ""
                    Toast.makeText(context, "Campos limpios", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Limpiar")
            }
            /*
            Button(
                onClick = {
                    viewModel.llamarGraphCoroutine(f.value)
                    showGraph.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
            ) {
                Text(text = "Graficar")
            }
             */

        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (bandera.value.isEmpty()) {
                //Text(text = "Al llenar todas las casillas, oprima el boton 'calcular'")
            } else {
                Secante(
                    a = a.value.toDouble(),
                    b = b.value.toDouble(),
                    f = f.value,
                    epsilon = x2.value.toDouble(),
                    maxIterations = MaxIter.value.toInt()
                )
            }

            if (showGraph.value) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FunctionGraph(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                    IconButton(
                        onClick = { showGraph.value = false },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close Graph")
                    }
                }
            }
        }
    }
}
@Composable
fun PasoBodySecante() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(2.dp)
            .background(colorScheme.background)
            .navigationBarsPadding()
            .padding(4.dp)
    ) {
        Text(
            text = "Avanza a tu propio ritmo",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )


        val f = remember { mutableStateOf("") }
        val x0 = remember { mutableStateOf("") }
        val x1 = remember { mutableStateOf("") }
        val error = remember { mutableStateOf("") }
        val MaxIter = remember { mutableStateOf("") }
        val context = LocalContext.current

        // Sección 1: Ingreso de función
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "1. Ingrese la función",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "Ejemplo de una función bien formada:",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "f(x) = 2*sin(x) + log(x, 10) − 3*(x^2) + pi",
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

        OutlinedTextField(
            label = { Text(text = "Ingrese la función") },
            value = f.value,
            onValueChange = { newValue -> f.value = newValue },
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Sección 2: Ingreso de parámetros
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("2. Ingrese ")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append("x₀, x₁")
                        }
                        append(", número máximo de iteraciones y una tolerancia. El algoritmo finaliza si se alcanza el máximo de iteraciones o si el error relativo es menor o igual a la tolerancia:\n\n")
                        withStyle(
                            style = SpanStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp,
                                background = MaterialTheme.colorScheme.surfaceVariant,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            append("|xₙ₊₁ − xₙ| / |xₙ₊₁| < tolerancia")
                        }
                    },
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                label = { Text(text = "x0") },
                value = x0.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        x0.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                label = { Text(text = "x1") },
                value = x1.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        x1.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                label = { Text(text = "Iter. Max") },
                value = MaxIter.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        MaxIter.value = newValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                label = { Text(text = "Tolerancia") },
                value = error.value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        error.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.weight(1f)
            )
        }

        var currentIndex by remember { mutableIntStateOf(0) }
        val results = remember { mutableStateListOf<ResultadoSecante>() }
        var shouldContinue by remember { mutableStateOf(true) }

        // Mostrar resultados de iteraciones previas
        if (results.isNotEmpty()) {
            Text(
                text = "Resultados de las iteraciones:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                color = colorScheme.onBackground
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .heightIn(max = 900.dp)
            ) {
                items(results) { resultado ->
                    IteracionResultCard(
                        resultado = resultado,
                        funcionExpresion = f.value,
                        tolerancia = error.value,
                        colorScheme = colorScheme
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Área de cálculo actual
        if (shouldContinue) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Fórmula usada:",
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("${currentIndex + 3}. Dado:\n\n")

                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("x₀ = ${x0.value}\n")
                                        append("x₁ = ${x1.value}\n")
                                    }

                                    append("\nSe calcula el nuevo punto x${currentIndex + 2} con la fórmula:\n\n")

                                    withStyle(
                                        SpanStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 16.sp,
                                            color = colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        append("x${currentIndex + 2} = x₁ - ((x₁ - x₀) / (f(x₁) - f(x₀))) × f(x₁)")
                                    }
                                },
                                textAlign = TextAlign.Justify,
                                color = colorScheme.onBackground,
                                modifier = Modifier.fillMaxWidth()
                            )

                        }
                    }



                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            try {
                                val x0Double = x0.value.toDoubleOrNull()
                                val x1Double = x1.value.toDoubleOrNull()
                                val tolerancia = error.value.toDoubleOrNull()
                                val maxIter = MaxIter.value.toIntOrNull()

                                if (x0Double == null || x1Double == null || tolerancia == null || maxIter == null || maxIter <= 0) {
                                    Toast.makeText(context, "Verifique los campos ingresados", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val (resultado, errorMsg) = metodoSecantePaso(
                                    x0Double,
                                    x1Double,
                                    f.value,
                                    currentIndex,
                                    tolerancia,
                                    ::evaluarFuncion
                                )

                                if (errorMsg != null) {
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                resultado?.let {
                                    results.add(it)
                                    x0.value = it.x1.toString()
                                    x1.value = it.x2.toString()
                                    currentIndex++

                                    shouldContinue = currentIndex < maxIter && it.errorRelativo > tolerancia

                                    when {
                                        !shouldContinue && it.errorRelativo <= tolerancia ->
                                            Toast.makeText(context, "Convergencia alcanzada", Toast.LENGTH_SHORT).show()
                                        !shouldContinue ->
                                            Toast.makeText(context, "Máximo de iteraciones alcanzado", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            } catch (e: Exception) {
                                Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Calcular x${currentIndex + 2}")
                    }

                }
            }
        } else {
            // Mostrar resultado final
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Resultado Final",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "La raíz de la ecuación f(x) = ${f.value} es:",
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "x ≈ ${results.lastOrNull()?.x2?.toString() ?: "No disponible"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Error relativo: ${results.lastOrNull()?.errorRelativo?.let { formatearValor(it) } ?: "No disponible"}",
                        color = colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Total de iteraciones: $currentIndex",
                        color = colorScheme.onSecondaryContainer
                    )
                }
            }

            Button(
                onClick = {
                    // Reiniciar todas las variables
                    f.value = ""
                    x0.value = ""
                    x1.value = ""
                    error.value = ""
                    MaxIter.value = ""
                    currentIndex = 0
                    results.clear()
                    shouldContinue = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Realizar nuevo cálculo")
            }
        }
    }
}

fun metodoSecantePaso(
    x0: Double,
    x1: Double,
    fStr: String,
    currentIndex: Int,
    tolerancia: Double,
    evaluarFuncion: (String, String) -> Double?,
): Pair<ResultadoSecante?, String?> {
    val fx0 = evaluarFuncion(x0.toString(), fStr)
    val fx1 = evaluarFuncion(x1.toString(), fStr)

    if (fx0 == null || fx1 == null) {
        return null to "Error al evaluar la función. Verifique la expresión."
    }

    if (fx1 == fx0) {
        return null to "División por cero: f(x0) y f(x1) son iguales."
    }

    val x2 = x1 - (fx1 * (x1 - x0) / (fx1 - fx0))
    val error = abs(x2 - x1) / abs(x2)

    val resultado = ResultadoSecante(
        iteracion = currentIndex,
        x0 = x0,
        x1 = x1,
        x2 = x2,
        errorRelativo = error
    )

    return resultado to null // null indica que no hubo error
}


@Composable
fun formatearValor(valor: Double): String {
    return if ((valor >= 1e6 || (valor <= 1e-4 && valor != 0.0))) {
        // Formatear con notación científica y luego convertir "E" a "×10^"
        val valorFormateado = String.format(Locale.US, "%.6e", valor)
        val partes = valorFormateado.split("e", "E")
        val base = partes[0].trimEnd('0').trimEnd('.')
        val exponente = partes[1].toInt()  // Puede tener signo
        "$base×10^$exponente"
    } else {
        // Mostrar como número decimal con hasta 6 cifras significativas
        val formatter = DecimalFormat("0.######", DecimalFormatSymbols(Locale.US))
        formatter.format(valor)
    }
}



@SuppressLint("DefaultLocale")
@Composable
fun IteracionResultCard(
    resultado: ResultadoSecante,
    funcionExpresion: String,
    tolerancia: String,
    colorScheme: ColorScheme
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(

                text = "Iteración ${resultado.iteracion + 1}",
                color = colorResource(id = R.color.rojounicauca),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tabla de valores
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surface, RoundedCornerShape(8.dp))
                    .border(1.dp, colorScheme.outline, RoundedCornerShape(8.dp))
            ) {
                // Encabezados
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.azulunicauca))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "x0",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "x1",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "x${resultado.iteracion + 2}",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Valores
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {


                    val x0Formatted = formatearValor(resultado.x0)
                    val x1Formatted = formatearValor(resultado.x1)
                    val x2Formatted = formatearValor(resultado.x2)


                    Text(
                        text = x0Formatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = x1Formatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = x2Formatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.rojounicauca),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Encabezados f(x)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.azulunicauca))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "f(x0)",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "f(x1)",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "f(x${resultado.iteracion + 2})",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Valores f(x)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    val fx0 = evaluarFuncion(resultado.x0.toString(), funcionExpresion) ?: 0.0
                    val fx1 = evaluarFuncion(resultado.x1.toString(), funcionExpresion) ?: 0.0
                    val fx2 = evaluarFuncion(resultado.x2.toString(), funcionExpresion) ?: 0.0

                    val fx0Formatted = formatearValor(fx0)
                    val fx1Formatted = formatearValor(fx1)
                    val fx2Formatted = formatearValor(fx2)


                    Text(
                        text = fx0Formatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = fx1Formatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = fx2Formatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.rojounicauca),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Análisis del error
            val errorCalculado = resultado.errorRelativo
            val toleranciaDouble = tolerancia.toDoubleOrNull() ?: 0.0

            val errorFormatted = formatearValor(errorCalculado)
            val toleranciaFormatted = formatearValor(toleranciaDouble)

            Text(
                text = "Error relativo: |x${resultado.iteracion + 2} - x1| / |x${resultado.iteracion + 2}| = $errorFormatted",
                fontSize = 14.sp,
                color = colorScheme.onSurfaceVariant
            )

            val haConvergido = errorCalculado <= toleranciaDouble

            Text(
                text = if (haConvergido)
                    "El error es menor que la tolerancia ($toleranciaFormatted). Convergencia alcanzada."
                else
                    "El error es mayor que la tolerancia ($toleranciaFormatted). Se continúa el proceso.",
                fontSize = 14.sp,
                fontWeight = if (haConvergido) FontWeight.Bold else FontWeight.Normal,
                color = if (haConvergido) colorResource(id = R.color.rojounicauca) else colorScheme.onSurfaceVariant
            )

        }
    }
}


@Composable
fun MathFormulaView(latex: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp), // ajusta la altura según necesidad
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                loadDataWithBaseURL(
                    null,
                    """
                    <html>
                      <head>
                        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.15.1/dist/katex.min.css">
                        <script defer src="https://cdn.jsdelivr.net/npm/katex@0.15.1/dist/katex.min.js"></script>
                        <script defer>
                          document.addEventListener("DOMContentLoaded", function() {
                            katex.render("$latex", document.getElementById("formula"), {
                              throwOnError: false
                            });
                          });
                        </script>
                      </head>
                      <body style="margin:0;padding:0;">
                        <div id="formula" style="font-size: 1.2em; padding: 10px;"></div>
                      </body>
                    </html>
                    """.trimIndent(),
                    "text/html", "utf-8", null
                )
            }
        }
    )
}


@SuppressLint("DefaultLocale")
fun calcularX2Secante(x0: Double, x1: Double, f: String): Double {

    val fx0 = evaluarFuncion(x0.toString(), f)
    val fx1 = evaluarFuncion(x1.toString(), f)
    if (fx1 - fx0 == 0.0) {
        throw IllegalArgumentException("División por cero detectada durante el cálculo.")
    }
    val x2 = x1 - (((x1 - x0) / (fx1 - fx0)) * fx1)

    val roundx2 = String.format("%.4f", x2)

    return roundx2.toDouble()
}

@SuppressLint("DefaultLocale")
fun evaluarFuncion(a: String, f: String): Double {
    val f = Function("f", f, "x")
    val fa = org.mariuszgromada.math.mxparser.Expression("f(${a})", f).calculate()
    val roundfa = String.format("%.4f", fa)
    return roundfa.toDouble()
}

@Composable
fun CurvedBorderText(
    text: String,
    textColor: Color = Color.White,
    backgroundColor: Color = colorResource(id = R.color.azulunicauca),
    borderColor: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    borderWidth: Dp = 1.dp,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = TextStyle.Default,
    fontSize: TextUnit = TextUnit.Unspecified,
    paddingStart: Dp = 8.dp,
    paddingEnd: Dp = 8.dp,
    paddingTop: Dp = 8.dp,
    paddingBottom: Dp = 8.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = textColor,
        fontWeight = fontWeight,
        textAlign = textAlign,
        style = style.copy(fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize),
        modifier = modifier
            .fillMaxSize()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(borderRadius)
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(borderRadius)
            )
            .padding(
                start = paddingStart,
                end = paddingEnd,
                top = paddingTop,
                bottom = paddingBottom
            )
    )
}

/*
HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
 */

