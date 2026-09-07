package com.example.mathsus.ui.methods.falsiMethod

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathsus.ui.features.nav_menu_bisection.ResultadoFalsi
import com.example.mathsus_ia.ui.methods.secanteMethod.CurvedBorderText
import com.example.mathsus_ia.ui.methods.secanteMethod.evaluarFuncion
import io.github.jesusgurrute.mathsus_ia.R
import kotlin.math.abs

@Composable
fun BodyFalsi() {
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
        Row(
            modifier = Modifier.fillMaxWidth(), // Asegura que ocupe todo el ancho
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado uniforme entre elementos
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
                    bandera1.value = ""
                    MaxIter.value = ""
                    Toast.makeText(context, "Campos limpios", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(text = "Limpiar")
            }
        }
        if (bandera1.value.isEmpty()) {
            /*
            Text(
                text = "Al llenar todas las casillas, oprima el boton 'calcular'",
                color = colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
             */

        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RegularFalsi(
                    f = funcion.value,
                    a = a.value.toDouble(),
                    b = b.value.toDouble(),
                    MaxIter = MaxIter.value.toInt(),
                    epsilon = error.value.toDouble(),
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PasoBodyFalsi() {
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
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
        )
        val funcion = remember { mutableStateOf("") }
        val a = remember { mutableStateOf("") }
        val b = remember { mutableStateOf("") }
        val tol = remember { mutableStateOf("") }
        val MaxIter = remember { mutableStateOf("") }
        val bandera = remember { mutableStateOf("") }
        val context = LocalContext.current

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
            value = funcion.value,
            onValueChange = {
                if (funcion.value.length <= 30)
                    funcion.value = it
            },
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Agrega un margen lateral si es necesario
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "2. Este método requiere un intervalo [a,b] donde la función sea continua y exista un cambio de signo. Además, un valor de tolerancia y máximo de iteraciones que permite finalizar el proceso.",
                color = colorScheme.onBackground,
                textAlign = TextAlign.Justify
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    .weight(1f)  // Ligeramente más ancho
                    .height(60.dp)
            )
            OutlinedTextField(
                label = { Text(text = "tol") },
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
                modifier = Modifier
                    .weight(1.5f)  // Ligeramente más ancho
                    .height(60.dp)
            )
        }
        var currentIndex by remember { mutableIntStateOf(0) }
        val results = remember { mutableStateListOf<ResultadoFalsi>() }
        var shouldContinue by remember { mutableStateOf(true) }

        results.forEach { resultado ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Iteración: ${resultado.iteracion}",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .width(400.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .fillMaxWidth()
                                    .width(IntrinsicSize.Min)
                                    .horizontalScroll(rememberScrollState())
                                    .background(colorScheme.background)
                            ) {
                                CurvedBorderText(
                                    text = "a",
                                    textColor = Color.White,
                                    backgroundColor = colorResource(id = R.color.azulunicauca),
                                    fontSize = 12.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "b",
                                    textColor = Color.White, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.azulunicauca),
                                    fontSize = 12.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "c${resultado.iteracion}",
                                    textColor = colorScheme.primary, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.azulunicauca),
                                    fontSize = 12.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "f(a)",
                                    textColor = Color.White, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.azulunicauca),
                                    fontSize = 12.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "f(b)",
                                    textColor = Color.White, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.azulunicauca),
                                    fontSize = 12.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "f(c${resultado.iteracion})",
                                    textColor = colorScheme.primary, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.azulunicauca),
                                    fontSize = 12.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .width(IntrinsicSize.Min)
                                    .horizontalScroll(rememberScrollState())
                                    .background(
                                        shape = RoundedCornerShape(4.dp),
                                        color = colorResource(id = R.color.grisunicauca)
                                    )
                            ) {
                                CurvedBorderText(
                                    text = "${resultado.a}",
                                    textColor = colorScheme.onSurface, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.grisunicauca),
                                    fontSize = 10.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "${resultado.b}",
                                    textColor = colorScheme.onSurface, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.grisunicauca),
                                    fontSize = 10.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = resultado.c.toString(),
                                    textColor = colorScheme.primary,
                                    backgroundColor = colorResource(id = R.color.grisunicauca),
                                    fontSize = 10.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "${evaluarFuncion(resultado.a.toString(), funcion.value)}",
                                    textColor = colorScheme.onSurface, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.grisunicauca),
                                    fontSize = 10.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "${evaluarFuncion(resultado.b.toString(), funcion.value)}",
                                    textColor = colorScheme.onSurface, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.grisunicauca),
                                    fontSize = 10.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                                CurvedBorderText(
                                    text = "${evaluarFuncion(resultado.c.toString(), funcion.value)}",
                                    textColor = colorScheme.primary, // Color del texto personalizado
                                    backgroundColor = colorResource(id = R.color.grisunicauca),
                                    fontSize = 10.sp,
                                    paddingStart = 12.dp,
                                    paddingEnd = 12.dp,
                                    paddingTop = 6.dp,
                                    paddingBottom = 6.dp,
                                    borderColor = colorScheme.onSurface,
                                    borderWidth = 1.dp, // Grosor del borde
                                    modifier = Modifier
                                        .weight(1f)
                                        .wrapContentSize(Alignment.Center)
                                )
                            }
                        }
                    }
                    val previousA = resultado.a
                    val errorf = abs(resultado.c.toString().toDouble() - previousA) / resultado.c.toString().toDouble()

                    Spacer(modifier = Modifier.height(16.dp))
                    if (evaluarFuncion(resultado.a.toString(), funcion.value) * evaluarFuncion(resultado.c.toString(), funcion.value) < 0) {
                        Text(
                            text = "Observé que f(c${resultado.iteracion}) < f(b) y el signo de f(a) con el signo de f(c${resultado.iteracion}) son opuestos, por lo que el nuevo intervalo se define como [a,c]. De este modo, el valor de 'b' toma el valor de c${resultado.iteracion}.",
                            color = colorScheme.onBackground,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Justify
                        )
                    } else {
                        Text(
                            text = "Observé que f(a) < f(c${resultado.iteracion}) y el signo de f(c${resultado.iteracion}) con el signo f(b) son opuestos, por lo que el nuevo intervalo se define como [c,b]. De este modo, el valor de 'a' toma el valor de c${resultado.iteracion}.",
                            color = colorScheme.onBackground,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Justify
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ademas:",
                        color = colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Text(
                                text = "       (c${resultado.iteracion} - c${resultado.iteracion-1}) \n" +
                                        "error =    ----------     \n  " +
                                        "     c${resultado.iteracion}", modifier = Modifier.fillMaxWidth(),
                                color = colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))


                        // In the iteration result display


                        Box {
                            Text(
                                text = "       (${resultado.c.toString().toDouble()} - ${previousA}) \n" +
                                        "=    ----------   =  \n  " +
                                        "  ${resultado.c.toString().toDouble()}   ", modifier = Modifier.fillMaxWidth(),
                                color = colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        }

                        Box {
                            val relativeError = abs(resultado.c - resultado.previousA) / resultado.c
                            Text(
                                text = " = ${String.format("%.4f", relativeError)}",
                                modifier = Modifier.fillMaxWidth(),
                                color = colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Aquí es donde hacemos el cambio

                    if (errorf < tol.value.toDouble() || resultado.iteracion == MaxIter.value.toInt()) {

                        Text(
                            text = "aquí ${String.format("%.4f", errorf)} < ${tol.value.toDouble()}, por lo que el error es menor al permitido. De manera que, se da por terminado el proceso.",
                            color = colorScheme.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    } else {
                        Text(
                            text = "tal que ${tol.value.toDouble()} < ${String.format("%.4f", errorf)}  , por lo que se procede a calcular c${resultado.iteracion + 1}.",
                            color = colorScheme.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

        }
        if (shouldContinue) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "${currentIndex + 3}. Dado el intervalo [a,b] = [${a.value},${b.value}]:\n" +
                            "\nse calcula la cuerda que une los dos puntos de f(a) y f(b) de la sigueinte manera:",
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Justify
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Text(
                        text = "       (a - b)*fb \n" +
                                "c${currentIndex + 1} =    ----------   =  \n  " +
                                "     fa-fb", modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
                Box {
                    Text(
                        text = "(${a.value} + ${b.value})*${evaluarFuncion(b.value, funcion.value)} \n" +
                                "      ----------       \n  " +
                                "${evaluarFuncion(a.value, funcion.value)} - ${evaluarFuncion(b.value, funcion.value)}",
                        modifier = Modifier.fillMaxWidth(),
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        try {
                            if (a.value.isEmpty() || b.value.isEmpty() || funcion.value.isEmpty() ||
                                tol.value.isEmpty() || MaxIter.value.isEmpty()) {
                                Toast.makeText(context, "No deje datos vacíos", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            bandera.value = a.value
                            Toast.makeText(context, "Calculando x${currentIndex + 2}", Toast.LENGTH_SHORT).show()

                            val aDouble = a.value.toDouble()
                            val bDouble = b.value.toDouble()

                            val faCheck = evaluarFuncion(aDouble.toString(), funcion.value)
                            val fbCheck = evaluarFuncion(bDouble.toString(), funcion.value)
                            if (faCheck * fbCheck > 0) {
                                Toast.makeText(
                                    context,
                                    "La función debe tener signos opuestos en los puntos a y b",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@Button
                            }

                            // Calculamos el nuevo punto
                            val result = calcularX2Falsi(
                                a = aDouble,
                                b = bDouble,
                                funcion = funcion.value
                            )

                            // Calculamos el error relativo con protección contra NaN
                            val previoA = if (results.isEmpty()) aDouble else results.last().c
                            val errorf = if (result != 0.0) {
                                abs(result - previoA) / abs(result)
                            } else {
                                abs(result - previoA) // Si result es 0, usamos solo la diferencia absoluta
                            }

                            // Verificar si el error es válido
                            if (errorf.isNaN()) {
                                Toast.makeText(
                                    context,
                                    "Error: El cálculo del error produjo un valor inválido",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@Button
                            }

                            // Agregamos el resultado a la lista
                            results.add(
                                ResultadoFalsi(
                                    currentIndex + 1,
                                    aDouble,
                                    bDouble,
                                    result,
                                    previoA  // Guardamos el valor previo
                                )
                            )

                            currentIndex++

                            // Evaluamos la función en los puntos necesarios
                            val fa = evaluarFuncion(aDouble.toString(), funcion.value)
                            val fc = evaluarFuncion(result.toString(), funcion.value)

                            // Actualizar el intervalo según el signo de f(a)*f(c)
                            if (fa * fc < 0) {
                                b.value = result.toString()
                            } else {
                                a.value = result.toString()
                            }

                            // Verificamos si debemos continuar
                            shouldContinue = currentIndex < MaxIter.value.toInt() && errorf > tol.value.toDouble()

                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Error en el cálculo: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                    enabled = shouldContinue
                ) {
                    val aux = currentIndex + 2
                    Text(text = "Iteración ${aux - 1}")
                }
            }


        } else {
            Column {

                Text(
                    text = "La raíz de la ecuación f(x) = ${funcion.value} es:",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = colorScheme.onBackground,
                )
                Text(
                    text = "x ≈ ${results.lastOrNull()?.c ?: "No disponible"}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorScheme.onBackground,
                )
            }
        }
        if (!shouldContinue) {
            Text(
                text = "El proceso ha terminado.",
                color = colorScheme.onBackground,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    // Reiniciar todas las variables
                    funcion.value = ""
                    a.value = ""
                    b.value = ""
                    tol.value = ""
                    MaxIter.value = ""
                    bandera.value = ""
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

@SuppressLint("DefaultLocale")
fun calcularX2Falsi(a: Double, b: Double, funcion: String): Double {
    val fa = evaluarFuncion(a.toString(), funcion) ?: return Double.NaN
    val fb = evaluarFuncion(b.toString(), funcion) ?: return Double.NaN

    // Evitar división por cero
    if (fb - fa == 0.0) {
        return a // o b, dependiendo de cuál consideremos mejor en este caso
    }

    // Fórmula del método de falsa posición
    val x2 = b - (fb * (b - a)) / (fb - fa)

    // Verificar si el resultado es válido
    return if (x2.isNaN() || x2.isInfinite()) {
        Double.NaN
    } else {
        String.format("%.4f", x2).toDouble()
    }
}