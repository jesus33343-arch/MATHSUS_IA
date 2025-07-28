package com.example.mathsus.ui.methods.bisectionMethod

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathsus.ui.features.nav_menu_bisection.ResultadoBisection
import com.example.mathsus.ui.features.nav_menu_secante.ResultadoSecante
import com.example.mathsus_ia.ui.methods.secanteMethod.CurvedBorderText
import com.example.mathsus_ia.ui.methods.secanteMethod.evaluarFuncion
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.methods.bisectionMethod.Bisection
import com.example.mathsus_ia.ui.methods.secanteMethod.formatearValor
import com.google.android.libraries.intelligence.acceleration.Analytics
import kotlin.math.abs

// CLASE DE DATOS CORREGIDA
data class ResultadoBisectionCorregida(
    val iteracion: Int,
    val a: Double,
    val b: Double,
    val c: Double, // punto medio actual
    val previousC: Double, // punto medio anterior
    val errorRelativo: Double // error relativo calculado
)

@Composable
fun BodyBisection() {
    val colorScheme = MaterialTheme.colorScheme
    val funcion = remember { mutableStateOf("") }
    val a = remember { mutableStateOf("") }
    val b = remember { mutableStateOf("") }
    val MaxIter = remember { mutableStateOf("") }
    val error = remember { mutableStateOf("") }
    val bandera1 = remember { mutableStateOf("") }
    val bandera2 = remember { mutableStateOf("") }

    val context = LocalContext.current
    val zoom = remember { mutableFloatStateOf(1f) }

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
            value = funcion.value,
            onValueChange = {
                if (funcion.value.length <= 30)
                    funcion.value = it
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
                value = error.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        error.value = processedValue
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
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Button(
                onClick = {
                    if (a.value.isEmpty() || b.value.isEmpty() || error.value.isEmpty() || funcion.value.isEmpty() || MaxIter.value.isEmpty()) {
                        Toast.makeText(
                            context,
                            "No deje datos vacios",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        bandera1.value = a.value
                        Toast.makeText(context, "Calculando", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            ) {
                Text(text = "Calcular")
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Nuevo botón para limpiar los campos
            Button(
                onClick = {
                    funcion.value = ""
                    a.value = ""
                    b.value = ""
                    error.value = ""
                    MaxIter.value = ""
                    bandera1.value = ""
                    Toast.makeText(context, "Campos limpios", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Limpiar")
            }
        }
        if (bandera1.value.isEmpty()) {

        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Bisection(
                    f = funcion.value,
                    a = a.value.toDouble(),
                    b = b.value.toDouble(),
                    MaxIter.value.toInt(),
                    epsilon = error.value.toDouble()
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PasoBodyBisection() {
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

        val funcion = remember { mutableStateOf("") }
        val a = remember { mutableStateOf("") }
        val b = remember { mutableStateOf("") }
        val tol = remember { mutableStateOf("") }
        val MaxIter = remember { mutableStateOf("") }
        val bandera = remember { mutableStateOf("") }
        val context = LocalContext.current

        // UI de entrada de datos (mismo código que tenías)
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
            value = funcion.value,
            onValueChange = {
                if (funcion.value.length <= 30)
                    funcion.value = it
            },
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

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
                    text = "2. Este método necesita un intervalo [a, b] donde la función sea continua y cambie de signo. También se debe definir un valor de tolerancia y un número máximo de iteraciones para detener el proceso.",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(16.dp),
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
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
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
                label = { Text(text = "Iter. Máx.") },
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
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                label = { Text(text = "Tolerancia") },
                value = tol.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        tol.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.weight(1f)
            )
        }

        // VARIABLES CORREGIDAS PARA EL CÁLCULO
        var currentIndex by remember { mutableIntStateOf(0) }
        val results = remember { mutableStateListOf<ResultadoBisectionCorregida>() }
        var shouldContinue by remember { mutableStateOf(true) }
        var currentA by remember { mutableStateOf(0.0) }
        var currentB by remember { mutableStateOf(0.0) }

        // Mostrar resultados existentes
        results.forEach { resultado ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .width(400.dp)
                    ) {
                        IteracionResultCardBisectionCorregida(
                            resultado = resultado,
                            funcionExpresion = funcion.value,
                            tolerancia = tol.value,
                            colorScheme = colorScheme
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Card para la explicación del intervalo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Análisis del Intervalo",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (evaluarFuncion(resultado.a.toString(), funcion.value)!! *
                                evaluarFuncion(resultado.c.toString(), funcion.value)!! < 0) {
                                Text(
                                    text = "🔍 f(a) × f(m${resultado.iteracion}) < 0, por lo que la raíz está en [a, m${resultado.iteracion}]",
                                    color = colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Justify
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📍 Nuevo intervalo: b = m${resultado.iteracion} = ${formatearValor(resultado.c)}",
                                    color = colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Justify,
                                    fontWeight = FontWeight.Medium
                                )
                            } else {
                                Text(
                                    text = "🔍 f(m${resultado.iteracion}) × f(b) < 0, por lo que la raíz está en [m${resultado.iteracion}, b]",
                                    color = colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Justify
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📍 Nuevo intervalo: a = m${resultado.iteracion} = ${formatearValor(resultado.c)}",
                                    color = colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Justify,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card para el cálculo del error CORREGIDO
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = null,
                                    tint = colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Cálculo del Error Relativo",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Fórmula general
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorScheme.surface
                                    ),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Error = |m${resultado.iteracion} - m${resultado.iteracion - 1}| / |m${resultado.iteracion}|",
                                        modifier = Modifier.padding(12.dp),
                                        color = colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Valores numéricos
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorScheme.surface
                                    ),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Error = |${formatearValor(resultado.c)} - ${formatearValor(resultado.previousC)}| / |${formatearValor(resultado.c)}|",
                                        modifier = Modifier.padding(12.dp),
                                        color = colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Resultado final destacado
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (resultado.errorRelativo < tol.value.toDoubleOrNull() ?: 0.01)
                                            colorScheme.primaryContainer
                                        else
                                            colorScheme.errorContainer
                                    ),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (resultado.errorRelativo < tol.value.toDoubleOrNull() ?: 0.01)
                                                Icons.Default.CheckCircle else Icons.Default.Info,
                                            contentDescription = null,
                                            tint = if (resultado.errorRelativo < tol.value.toDoubleOrNull() ?: 0.01)
                                                colorScheme.onPrimaryContainer
                                            else
                                                colorScheme.onErrorContainer,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Error = ${formatearValor(resultado.errorRelativo)} (${String.format("%.4f", resultado.errorRelativo * 100)}%)",
                                            color = if (resultado.errorRelativo < tol.value.toDoubleOrNull() ?: 0.01)
                                                colorScheme.onPrimaryContainer
                                            else
                                                colorScheme.onErrorContainer,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Verificación de convergencia
                    val toleranciaDouble = tol.value.toDoubleOrNull() ?: 0.01
                    if (resultado.errorRelativo < toleranciaDouble || resultado.iteracion >= MaxIter.value.toIntOrNull() ?: 100) {
                        Text(
                            text = "✅ Error ${formatearValor(resultado.errorRelativo)} < ${formatearValor(toleranciaDouble)}, convergencia alcanzada.",
                            color = colorScheme.primary,
                            textAlign = TextAlign.Justify,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "⏭️ Error ${formatearValor(resultado.errorRelativo)} > ${formatearValor(toleranciaDouble)}, se continúa con la siguiente iteración.",
                            color = colorScheme.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }

        // Lógica para continuar con las iteraciones
        if (shouldContinue) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val aActual = if (results.isEmpty()) a.value else {
                    if (currentA != 0.0) currentA.toString() else a.value
                }
                val bActual = if (results.isEmpty()) b.value else {
                    if (currentB != 0.0) currentB.toString() else b.value
                }

                Text(
                    text = "${currentIndex + 3}. Intervalo actual [a, b] = [${formatearValor(aActual.toDoubleOrNull() ?: 0.0)}, ${formatearValor(bActual.toDoubleOrNull() ?: 0.0)}]:\n\nCalcular el punto medio m${currentIndex + 1}:",
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fórmula general
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "m${currentIndex + 1} = (a + b) / 2",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }

                // Evaluación con valores actuales
                Surface(
                    tonalElevation = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "m${currentIndex + 1} = (${formatearValor(aActual.toDoubleOrNull() ?: 0.0)} + ${formatearValor(bActual.toDoubleOrNull() ?: 0.0)}) / 2 = ${formatearValor(((aActual.toDoubleOrNull() ?: 0.0) + (bActual.toDoubleOrNull() ?: 0.0)) / 2)}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (funcion.value.isEmpty() || a.value.isEmpty() || b.value.isEmpty() ||
                            tol.value.isEmpty() || MaxIter.value.isEmpty()) {
                            Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val aVal = if (results.isEmpty()) a.value.toDoubleOrNull() ?: 0.0 else currentA
                        val bVal = if (results.isEmpty()) b.value.toDoubleOrNull() ?: 0.0 else currentB
                        val tolerancia = tol.value.toDoubleOrNull() ?: 0.01
                        val maxIteraciones = MaxIter.value.toIntOrNull() ?: 100

                        // Calcular punto medio
                        val puntoMedio = (aVal + bVal) / 2.0

                        // Calcular error relativo (si hay iteración anterior)
                        val errorRelativo = if (results.isNotEmpty()) {
                            val puntoMedioAnterior = results.last().c
                            abs(puntoMedio - puntoMedioAnterior) / abs(puntoMedio)
                        } else {
                            1.0 // Primera iteración, error alto
                        }

                        // Crear resultado
                        val resultado = ResultadoBisectionCorregida(
                            iteracion = currentIndex + 1,
                            a = aVal,
                            b = bVal,
                            c = puntoMedio,
                            previousC = if (results.isNotEmpty()) results.last().c else 0.0,
                            errorRelativo = errorRelativo
                        )

                        results.add(resultado)

                        // Actualizar intervalo para siguiente iteración
                        val fa = evaluarFuncion(aVal.toString(), funcion.value) ?: 0.0
                        val fc = evaluarFuncion(puntoMedio.toString(), funcion.value) ?: 0.0

                        if (fa * fc < 0) {
                            // La raíz está en [a, c]
                            currentA = aVal
                            currentB = puntoMedio
                        } else {
                            // La raíz está en [c, b]
                            currentA = puntoMedio
                            currentB = bVal
                        }

                        currentIndex++

                        // Verificar condiciones de parada
                        if (errorRelativo < tolerancia || currentIndex >= maxIteraciones) {
                            shouldContinue = false
                            Toast.makeText(
                                context,
                                if (errorRelativo < tolerancia)
                                    "¡Convergencia alcanzada!"
                                else
                                    "Máximo de iteraciones alcanzado",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Calcular Iteración ${currentIndex + 1}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Botón para reiniciar el proceso
        if (!shouldContinue || results.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        // Reiniciar todo
                        results.clear()
                        currentIndex = 0
                        shouldContinue = true
                        currentA = 0.0
                        currentB = 0.0
                        Toast.makeText(context, "Proceso reiniciado", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Reiniciar",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        // Limpiar campos
                        funcion.value = ""
                        a.value = ""
                        b.value = ""
                        tol.value = ""
                        MaxIter.value = ""
                        results.clear()
                        currentIndex = 0
                        shouldContinue = true
                        currentA = 0.0
                        currentB = 0.0
                        Toast.makeText(context, "Campos limpiados", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Limpiar Todo",
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun IteracionResultCardBisectionCorregida(
    resultado: ResultadoBisectionCorregida,
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
                text = "Iteración ${resultado.iteracion}",
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
                        text = "a",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "b",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "m${resultado.iteracion}",
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
                    val aFormatted = formatearValor(resultado.a)
                    val bFormatted = formatearValor(resultado.b)
                    val cFormatted = formatearValor(resultado.c)

                    Text(
                        text = aFormatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = bFormatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = cFormatted,
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
                        text = "f(a)",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "f(b)",
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "f(m${resultado.iteracion})",
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
                    val fa = evaluarFuncion(resultado.a.toString(), funcionExpresion) ?: 0.0
                    val fb = evaluarFuncion(resultado.b.toString(), funcionExpresion) ?: 0.0
                    val fc = evaluarFuncion(resultado.c.toString(), funcionExpresion) ?: 0.0

                    val faFormatted = formatearValor(fa)
                    val fbFormatted = formatearValor(fb)
                    val fcFormatted = formatearValor(fc)

                    Text(
                        text = faFormatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = fbFormatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = fcFormatted,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.rojounicauca),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Análisis del error (solo si no es la primera iteración)
            if (resultado.iteracion > 1) {
                val errorCalculado = resultado.errorRelativo
                val toleranciaDouble = tolerancia.toDoubleOrNull() ?: 0.0

                val errorFormatted = formatearValor(errorCalculado)
                val toleranciaFormatted = formatearValor(toleranciaDouble)

                Text(
                    text = "Error relativo: |m${resultado.iteracion} - m${resultado.iteracion - 1}| / |m${resultado.iteracion}| = $errorFormatted",
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
}