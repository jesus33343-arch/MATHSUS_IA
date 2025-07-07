package com.example.mathsus_ia.ui.methods.bisectionMethod

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathsus_ia.ui.methods.secanteMethod.CurvedBorderText
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.methods.Metodo
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.abs
import kotlin.math.sign

@SuppressLint("DefaultLocale")
@Composable
fun Bisection(
    f: String,
    a: Double,
    b: Double,
    MaxIter: Int,
    epsilon: Double,
) {
    val context = LocalContext.current

    // Initial validation
    val fa = Metodo(a = a, f = f)
    val fb = Metodo(a = b, f = f)

    if (fa * fb >= 0) {
        Toast.makeText(
            context,
            "The function must have opposite signs at points a and b",
            Toast.LENGTH_LONG
        ).show()
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .width(380.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Row
            TableHeader()

            // Iteration logic
            var currentXl = a
            var currentXu = b
            var previousXr = 0.0
            var iter = 0

            while (true) {
                // Calculate midpoint
                val currentXr = (currentXl + currentXu) / 2
                val fr = Metodo(a = currentXr, f = f)
                val fl = Metodo(a = currentXl, f = f)
                iter++

                // Calculate relative error
                val ea = if (currentXr != 0.0) {
                    abs((currentXr - previousXr) / currentXr) * 100
                } else {
                    100.0
                }

                // Display current iteration
                TableRow(
                    iteration = iter,
                    xl = currentXl,
                    xu = currentXu,
                    xr = currentXr,
                    error = ea
                )

                // Update interval
                when {
                    fr == 0.0 -> {
                        showResult(context, f, currentXr, iter)
                        return
                    }
                    sign(fl) * sign(fr) < 0 -> {
                        currentXu = currentXr
                    }
                    else -> {
                        currentXl = currentXr
                    }
                }

                // Check stopping criteria
                if (ea <= epsilon || iter >= MaxIter) {
                    showResult(context, f, currentXr, iter)
                    return
                }

                previousXr = currentXr
            }
        }
    }
}

@Composable
private fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.azulunicauca),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Box(modifier = Modifier.width(40.dp)) {
            HeaderCell(text = "n")
        }
        Box(modifier = Modifier.width(80.dp)) {
            HeaderCell(text = "a")
        }
        Box(modifier = Modifier.width(80.dp)) {
            HeaderCell(text = "b")
        }
        Box(modifier = Modifier.width(80.dp)) {
            HeaderCell(text = "c")
        }
        Box(modifier = Modifier.width(80.dp)) {
            HeaderCell(text = "ea")
        }
    }
}

@Composable
private fun HeaderCell(text: String) {
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
        modifier = Modifier.wrapContentSize(Alignment.Center)
    )
}

@Composable
private fun TableRow(
    iteration: Int,
    xl: Double,
    xu: Double,
    xr: Double,
    error: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.grisunicauca),
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Box(modifier = Modifier.width(40.dp).horizontalScroll(rememberScrollState())) {
            DataCell(text = iteration.toString())
        }
        Box(modifier = Modifier.width(80.dp).horizontalScroll(rememberScrollState())) {
            DataCell(FormatNumber(xl))
        }
        Box(modifier = Modifier.width(80.dp).horizontalScroll(rememberScrollState())) {
            DataCell(FormatNumber(xu))
        }
        Box(modifier = Modifier.width(80.dp).horizontalScroll(rememberScrollState())) {
            DataCell(FormatNumber(xr))
        }
        Box(modifier = Modifier.width(80.dp).horizontalScroll(rememberScrollState())) {
            DataCell(FormatNumber(error))
        }
    }
}
// Función formatNumber proporcionada
@SuppressLint("DefaultLocale")
fun FormatNumber(value: Double): String {
    return try {
        val formatted = String.format("%.4e", value)
        if (formatted.contains("e")) {
            val parts = formatted.split("e")
            val coefficient = parts[0].toDouble()
            val exponent = parts[1].toInt()
            val trimmedCoefficient = if (coefficient % 1 == 0.0) {
                coefficient.toInt().toString()
            } else {
                parts[0].replace(Regex("0*$"), "")
            }
            "$trimmedCoefficient × 10^$exponent"
        } else {
            String.format("%.4f", value)
        }
    } catch (e: Exception) {
        value.toString()
    }
}
@Composable
private fun DataCell(text: String) {
    CurvedBorderText(
        text = text,
        textColor = Color.Black,
        backgroundColor = colorResource(id = R.color.grisunicauca),
        fontSize = 10.sp,
        paddingStart = 12.dp,
        paddingEnd = 12.dp,
        paddingTop = 6.dp,
        paddingBottom = 6.dp,
        borderColor = Color.Black,
        borderWidth = 1.dp,
        modifier = Modifier.wrapContentSize(Alignment.Center)
    )
}

private fun showResult(context: Context, f: String, root: Double, iterations: Int) {
    Toast.makeText(
        context,
        "La raíz de la función de $f es: %.4f despues de $iterations iteraciones".format(root),
        Toast.LENGTH_LONG
    ).show()
}

/*
error = currentXu - currentXl

            while (iter <= 200) {
                error /= 2
                p = currentXl + error
                fp = Metodo(a = p, f = f)

                val roundc = String.format("%.4f", p)

                val roundfc = String.format("%.4e", fp)
                val parts = roundfc.split("e")
                val coefficient = parts[0].toDouble()
                val exponent = parts[1].toInt()
                // Quitar ceros adicionales si es necesario
                val trimmedCoefficient = if (coefficient % 1 == 0.0) {
                    coefficient.toInt().toString()
                } else {
                    parts[0].replace(Regex("0*$"), "")
                }

                val rounderror = String.format("%.4e", error)
                val partserror = rounderror.split("e")
                val coefficienterror = partserror[0].toDouble()
                val exponenterror = partserror[1].toInt()
                val errorCoefficient = if (coefficienterror % 1 == 0.0) {
                    coefficienterror.toInt().toString()
                } else {
                    partserror[0].replace(Regex("0*$"), "")
                }




                if (abs(error) < epsilon) {
                    Text("La función $f tiene raiz en $roundc")
                    return
                }

                if (fl.sign != Metodo(a = p, f = f).sign) {
                    currentXu = p
                    fu = Metodo(a = p, f = f)
                } else {
                    currentXl = p
                    fl = Metodo(a = p, f = f)
                }
                iter++
            }
 */
