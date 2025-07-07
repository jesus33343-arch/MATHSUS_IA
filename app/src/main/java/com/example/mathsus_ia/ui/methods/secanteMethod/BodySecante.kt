package com.example.mathsus_ia.ui.methods.secanteMethod

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mathsus.ui.features.nav_menu_secante.ResultadoSecante
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.methods.FunctionGraph
import com.example.mathsus_ia.ui.methods.GraphViewModel
import org.mariuszgromada.math.mxparser.Function
import kotlin.math.abs

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
            label = { Text(text = "Ingrese la funcion") },
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
            color = colorScheme.onBackground,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "1. Ingrese la función",
                    textAlign = TextAlign.Justify,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ejemplo de una función bien formada:",
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "f(x) = 2 * sin(x) + log(x, 10) - 3*x^2 + pi",
                    modifier = Modifier.fillMaxWidth(),
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
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
            Text(
                text = "2. Elija los puntos iniciales x0, x1, el número máximo de iteraciones y un valor de tolerancia. Si |Xnuevo - Xanterior|/Xnuevo < tolerancia, el proceso termina.",
                color = colorScheme.onBackground,
                textAlign = TextAlign.Justify
            )
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
                    Text(
                        text = "${currentIndex + 3}. Dado:\n" +
                                "\nx0 = ${x0.value}\n" +
                                "x1 = ${x1.value}\n" +
                                "\nSe calcula el punto de corte x${currentIndex + 2} con la siguiente ecuación:",
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "x${currentIndex + 2} = x1 - ((x1 - x0) / (f(x1) - f(x0))) * f(x1)",
                        modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (x0.value.isEmpty() || x1.value.isEmpty() || f.value.isEmpty() ||
                                error.value.isEmpty() || MaxIter.value.isEmpty()
                            ) {
                                Toast.makeText(
                                    context,
                                    "Complete todos los campos antes de calcular",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            try {
                                val x0Double = x0.value.toDouble()
                                val x1Double = x1.value.toDouble()
                                val tolerancia = error.value.toDouble()
                                val maxIteraciones = MaxIter.value.toIntOrNull() ?: 0

                                if (maxIteraciones <= 0) {
                                    Toast.makeText(
                                        context,
                                        "El número máximo de iteraciones debe ser positivo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                // Calcular el nuevo punto x2
                                val fx0 = evaluarFuncion(x0Double.toString(), f.value) ?: 0.0
                                val fx1 = evaluarFuncion(x1Double.toString(), f.value) ?: 0.0

                                if (fx1 == fx0) {
                                    Toast.makeText(
                                        context,
                                        "Error: División por cero. Los valores de la función coinciden en x0 y x1.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@Button
                                }

                                val x2Double = x1Double - ((x1Double - x0Double) / (fx1 - fx0)) * fx1

                                // Calcular error relativo
                                val errorCalculado = abs(x2Double - x1Double) / abs(x2Double)

                                // Agregar el resultado a la lista
                                results.add(
                                    ResultadoSecante(
                                        iteracion = currentIndex,
                                        x0 = x0Double,
                                        x1 = x1Double,
                                        x2 = x2Double,
                                        errorRelativo = errorCalculado
                                    )
                                )

                                // Preparar para la siguiente iteración
                                x0.value = x1Double.toString()
                                x1.value = x2Double.toString()
                                currentIndex++

                                // Verificar si debemos continuar
                                shouldContinue = currentIndex < maxIteraciones && errorCalculado > tolerancia

                                if (!shouldContinue && errorCalculado <= tolerancia) {
                                    Toast.makeText(
                                        context,
                                        "Convergencia alcanzada: Error < Tolerancia",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (!shouldContinue) {
                                    Toast.makeText(
                                        context,
                                        "Se alcanzó el número máximo de iteraciones",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error en el cálculo: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
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
                        text = "Error relativo: ${results.lastOrNull()?.errorRelativo?.let { String.format("%.8f", it) } ?: "No disponible"}",
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
                    Text(
                        text = String.format("%.6f", resultado.x0),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = String.format("%.6f", resultado.x1),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = String.format("%.6f", resultado.x2),
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

                    Text(
                        text = String.format("%.6f", fx0),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = String.format("%.6f", fx1),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = String.format("%.6f", fx2),
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

            Text(
                text = "Error relativo: |x${resultado.iteracion + 2} - x1| / |x${resultado.iteracion + 2}| = ${String.format("%.8f", errorCalculado)}",
                fontSize = 14.sp,
                color = colorScheme.onSurfaceVariant
            )

            Text(
                text = if (errorCalculado <= toleranciaDouble)
                    "El error es menor que la tolerancia (${tolerancia}). Convergencia alcanzada."
                else
                    "El error es mayor que la tolerancia (${tolerancia}). Se continúa el proceso.",
                fontSize = 14.sp,
                fontWeight = if (errorCalculado <= toleranciaDouble) FontWeight.Bold else FontWeight.Normal,
                color = if (errorCalculado <= toleranciaDouble) colorResource(id = R.color.rojounicauca) else colorScheme.onSurfaceVariant
            )
        }
    }
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

