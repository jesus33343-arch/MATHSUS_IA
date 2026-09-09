package com.example.mathsus.ui.methods.newtonMethod

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.mathsus_ia.ui.methods.LatexText
import com.example.mathsus_ia.ui.methods.functionExprToLatex
import com.example.mathsus_ia.ui.methods.rememberMathMarkwon
import com.example.mathsus_ia.ui.methods.secanteMethod.CurvedBorderText
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.features.nav_menu_newton.ResultadoNewton
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression
import java.util.Locale
import kotlin.math.abs


@Composable
fun BodyNewtonRaphson(initialFunction: String = "") {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp)
    ) {

        val funcion = rememberSaveable { mutableStateOf("") }
        LaunchedEffect(initialFunction) { if (initialFunction.isNotBlank()) funcion.value = initialFunction }
        val xk = rememberSaveable { mutableStateOf("") }
        val error = rememberSaveable { mutableStateOf("") }
        val bandera = rememberSaveable { mutableStateOf("") }
        val context = LocalContext.current
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
        if (funcion.value.isNotBlank()) {
            val markwon = rememberMathMarkwon()
            LatexText(
                latex = "\$\$f(x) = ${functionExprToLatex(funcion.value)}\$\$",
                color = colorScheme.onBackground,
                markwon = markwon,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 28.dp, max = 60.dp)
                    .padding(top = 6.dp, start = 4.dp)
            )
        }
        Row {
            OutlinedTextField(
                label = { Text(text = "X") },
                value = xk.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    // Primero reemplazamos las comas por puntos
                    val processedValue = newValue.replace(',', '.')
                    // Luego verificamos si el valor resultante es un número decimal válido
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        xk.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.size(width = 150.dp, height = 60.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                label = { Text(text = "Error") },
                value = error.value,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = { newValue ->
                    // Primero reemplazamos las comas por puntos
                    val processedValue = newValue.replace(',', '.')
                    // Luego verificamos si el valor resultante es un número decimal válido
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        error.value = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.size(width = 200.dp, height = 60.dp)
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Button(
                onClick = {
                    if (xk.value.isEmpty() || funcion.value.isEmpty()) {
                        Toast.makeText(context, "No deje datos vacios", Toast.LENGTH_SHORT).show()
                    } else {
                        bandera.value = xk.value
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
                    funcion.value = ""
                    xk.value = ""
                    error.value = ""
                    bandera.value = ""
                    Toast.makeText(context, "Campos limpios", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),

                ) {
                Text(text = "Limpiar")
            }


        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (bandera.value.isEmpty()) {
                //Text(text = "Al llenar todas las casillas, oprima el boton 'calcular'")
            } else {
                NewtonRaphson(
                    x = xk.value.toDouble(),
                    f = funcion.value,
                    error = error.value.toDouble()
                )
            }
        }
    }
}

@Composable
fun PasoBodyNewton() {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    var function by remember { mutableStateOf("") }
    var x0 by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var currentIndex by remember { mutableIntStateOf(0) }
    val results = remember { mutableStateListOf<ResultadoNewton>() }
    var shouldContinue by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(6.dp)
            .background(colorScheme.background)
            .navigationBarsPadding()
    ) {
        Text(
            text = "Avanza a tu propio ritmo",
            color = colorScheme.onBackground,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        // Input para la función
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
                    text = "f(x) = x^2 - 4",
                    modifier = Modifier.fillMaxWidth(),
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Este método es conciderado abierto, donde no requiere un intervalo, sino que un valor inicial. Además, se tendra implícita la derivada f'(x).",
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Justify
                )
            }
        }
        OutlinedTextField(
            label = { Text(text = "Ingrese la función") },
            value = function,
            onValueChange = {
                if (it.length <= 30)
                    function = it
            },
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        // Input para x0 y error
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "2. Ingrese el valor inicial x0 y el error deseado para terminar el cálculo.",
                color = colorScheme.onBackground,
                textAlign = TextAlign.Justify
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(
                label = { Text(text = "x0") },
                value = x0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        x0 = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.size(width = 80.dp, height = 60.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                label = { Text(text = "Error") },
                value = error,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { newValue ->
                    val processedValue = newValue.replace(',', '.')
                    if (processedValue.isEmpty() || processedValue.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                        error = processedValue
                    }
                },
                shape = RoundedCornerShape(size = 8.dp),
                modifier = Modifier.size(width = 130.dp, height = 60.dp)
            )
        }
        if (function.isNotEmpty()) {
            MostrarFuncionYDerivada(function, results)
        }
        results.forEach { resultado ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Iteración: ${resultado.iteracion}",
                        color = colorScheme.primary,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Bold
                    )


                    Box(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .width(300.dp)
                    ) {
                        Column(modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxWidth()
                            .width(IntrinsicSize.Min)
                            .horizontalScroll(rememberScrollState())
                            .background(MaterialTheme.colorScheme.background)) {
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .fillMaxWidth()
                                    .width(IntrinsicSize.Min)
                                    .horizontalScroll(rememberScrollState())
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                ResultHeaderCell(text = "x${resultado.iteracion} ")
                                ResultHeaderCell(text = "f(x${resultado.iteracion})")
                                ResultHeaderCell(text = "f'(x${resultado.iteracion})")
                                ResultHeaderCell(text = "x${resultado.iteracion + 1}")
                            }
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .fillMaxWidth()
                                    .width(IntrinsicSize.Min)
                                    .horizontalScroll(rememberScrollState())
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                ResultCell(text = "${resultado.x}")
                                ResultCell(text = "${resultado.fx}")
                                ResultCell(text = "${resultado.dfx}")
                                ResultCell(text = "${resultado.nextX}")
                            }
                        }
                    }
                }
            }
        }

        if (shouldContinue) {
            CalculationSection(currentIndex, x0)
            CalculateButton(
                currentIndex,
                shouldContinue,
                { shouldContinue = it },
                { currentIndex = it },
                function,
                x0,
                error,
                results,
                context
            )
        }// Mostrar resultados
        else {
            FinalResult(function, results)
            RestartButton(
                { function = "" },
                { x0 = "" },
                { error = "" },
                { currentIndex = 0 },
                { results.clear() },
                { shouldContinue = true }
            )
        }
    }
}


@Composable
fun ResultHeaderCell(text: String) {
    CurvedBorderText(
        text = text,
        textColor = Color.White,
        backgroundColor = colorResource(id = R.color.azulunicauca),
        fontSize = 14.sp,
        paddingStart = 12.dp,
        paddingEnd = 12.dp,
        paddingTop = 6.dp,
        paddingBottom = 6.dp,
        borderColor = Color.Black,
        borderWidth = 1.dp,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun ResultCell(text: String) {
    CurvedBorderText(
        text = text,
        textColor = colorScheme.onSurface,
        backgroundColor = colorResource(id = R.color.grisunicauca),
        fontSize = 14.sp,
        paddingStart = 12.dp,
        paddingEnd = 12.dp,
        paddingTop = 6.dp,
        paddingBottom = 6.dp,
        borderColor = Color.Black,
        borderWidth = 1.dp,
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    )
}

@SuppressLint("DefaultLocale")
fun newtonRaphsonCalculator(iteracion: Int, x: Double, function: String, symbolicDerivative: String): ResultadoNewton {
    val fx = evaluarFuncion1(x, function)
    val dfx = calculateDerivative(function, x)

    if (dfx == 0.0) {
        throw IllegalArgumentException("La derivada es cero. No se puede continuar.")
    }

    val nextX = x - fx / dfx

    return ResultadoNewton(
        iteracion = iteracion,
        x = x,
        fx = String.format("%.4f", fx).toDouble(),
        dfx = String.format("%.4f", dfx).toDouble(),
        nextX = String.format("%.4f", nextX).toDouble(),
        symbolicDerivative = symbolicDerivative
    )
}
fun calculateSymbolicDerivative(function: String): String {
    // Remove whitespace and convert to lowercase for easier parsing
    val cleanFunction = function.replace("\\s".toRegex(), "").lowercase(Locale.getDefault())

    return when {
        cleanFunction == "sin(x)" -> "cos(x)"
        cleanFunction == "cos(x)" -> "-sin(x)"
        cleanFunction == "tan(x)" -> "sec^2(x)"
        cleanFunction == "exp(x)" || cleanFunction == "e^x" -> "exp(x)" // or "e^x"
        cleanFunction == "ln(x)" -> "1/x"
        cleanFunction.matches(Regex("x\\^(\\d+)")) -> {
            val power = cleanFunction.substringAfter("^").toInt()
            "${power}x^${power - 1}"
        }
        // Add more cases as needed
        else -> "f'(x) (calculada numéricamente)" // Fallback for unsupported functions
    }
}
// Esta función no es @Composable
fun evaluarFuncion1(x: Double, function: String): Double {
    val xArgument = Argument("x = $x")
    val expression = Expression(function, xArgument)
    return expression.calculate()
}

fun calculateDerivative(function: String, x: Double): Double {
    val xArgument = Argument("x = $x")
    val derivativeExpression = "der($function, x)"
    val expression = Expression(derivativeExpression, xArgument)
    return expression.calculate()
}

// Modificar CalculateButton para usar NewtonRaphsonCalculator
@Composable
fun CalculateButton(
    currentIndex: Int,
    shouldContinue: Boolean,
    onShouldContinueChange: (Boolean) -> Unit,
    onCurrentIndexChange: (Int) -> Unit,
    function: String,
    x0: String,
    error: String,
    results: SnapshotStateList<ResultadoNewton>,
    context: android.content.Context
) {
    Button(
        onClick = {
            if (x0.isEmpty() || function.isEmpty() || error.isEmpty()) {
                Toast.makeText(context, "No deje datos vacíos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Calculando x${currentIndex + 1}", Toast.LENGTH_SHORT).show()

                try {
                    val symbolicDerivative = calculateSymbolicDerivative(function)
                    val x = if (currentIndex == 0) x0.toDouble() else results.last().nextX
                    val result = newtonRaphsonCalculator(currentIndex, x, function, symbolicDerivative)
                    if (!result.nextX.isFinite() || !result.fx.isFinite()) {
                        Toast.makeText(context, "El cálculo produjo un valor no finito, no se puede continuar", Toast.LENGTH_LONG).show()
                        onShouldContinueChange(false)
                        return@Button
                    }
                    results.add(result)
                    onCurrentIndexChange(currentIndex + 1)

                    // Check if we should continue
                    onShouldContinueChange(currentIndex + 1 < 200 && abs(result.nextX - x) > error.toDouble())
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(context, e.message ?: "No se puede continuar", Toast.LENGTH_LONG).show()
                    onShouldContinueChange(false)
                }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
        enabled = shouldContinue
    ) {
        Text(text = "Calcular x${currentIndex + 1}")
    }
}

@Composable
fun CalculationSection(currentIndex: Int, x0: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "${currentIndex + 3}. Dado x${currentIndex} = ${if (currentIndex == 0) x0 else "valor previo"}, se calcula:",
            color = colorScheme.onBackground,
            textAlign = TextAlign.Justify
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "x${currentIndex + 1} = x${currentIndex} - f(x${currentIndex}) / f'(x${currentIndex})",
        modifier = Modifier.fillMaxWidth(),
        color = colorScheme.onBackground,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Para ello, oprima el botón",
                color = colorScheme.onBackground,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun FinalResult(function: String, results: List<ResultadoNewton>) {
    Column {
        Text(
            text = "La raíz de la ecuación f(x) = $function es:",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = colorScheme.onBackground,
        )
        Text(
            text = "x ≈ ${results.lastOrNull()?.nextX ?: "No disponible"}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = colorScheme.onBackground,
        )
    }
}

@Composable
fun RestartButton(
    onFunctionReset: () -> Unit,
    onX0Reset: () -> Unit,
    onErrorReset: () -> Unit,
    onCurrentIndexReset: () -> Unit,
    onResultsReset: () -> Unit,
    onShouldContinueReset: () -> Unit
) {
    Button(
        onClick = {
            onFunctionReset()
            onX0Reset()
            onErrorReset()
            onCurrentIndexReset()
            onResultsReset()
            onShouldContinueReset()
        },
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Realizar nuevo cálculo")
    }
}

@Composable
fun MostrarFuncionYDerivada(function: String, results: List<ResultadoNewton>) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, colorScheme.primary, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        FuncionText("f(x)", function)
        Spacer(modifier = Modifier.height(8.dp))
        if (results.isNotEmpty()) {
            FuncionText("f'(x)", results[0].symbolicDerivative)
        } else {
            FuncionText("f'(x)", "Pendiente de cálculo")
        }
    }
}

@Composable
fun FuncionText(label: String, content: String) {
    val colorScheme = MaterialTheme.colorScheme

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label =",
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = content,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

