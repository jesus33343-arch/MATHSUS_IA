package com.example.mathsus.ui.methods.falsiMethod

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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

@SuppressLint("DefaultLocale")
@Composable
fun RegularFalsi(
    f: String,
    a: Double,
    b: Double,
    MaxIter: Int,
    epsilon: Double,
) {
    var iter = 0
    val fl = Metodo(a = a, f = f)
    val fu = Metodo(a = b, f = f)
    val xr = a
    var ea = 100.0

    var currentXl = a
    var currentXu = b
    var currentXr = xr
    var currentFl = fl
    var currentFu = fu

    var il = 0
    var iu = 0

    val context = LocalContext.current

    // Validar que los puntos inicial y final tienen signos opuestos
    if (currentFl * currentXu > 0) {
        Toast.makeText(
            context,
            "La función debe tener signos opuestos en los puntos a y b",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .width(380.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                    text = "n",
                    textColor = Color.White,
                    backgroundColor = colorResource(id = R.color.azulunicauca),
                    fontSize = 14.sp,
                    paddingStart = 6.dp,
                    paddingEnd = 6.dp,
                    paddingTop = 6.dp,
                    paddingBottom = 6.dp,
                    borderColor = Color.Black,
                    borderWidth = 1.dp, // Grosor del borde
                    modifier = Modifier
                        .weight(0.3f)
                        .wrapContentSize(Alignment.Center)
                )
                CurvedBorderText(
                    text = "a",
                    textColor = Color.White,
                    backgroundColor = colorResource(id = R.color.azulunicauca),
                    fontSize = 14.sp,
                    paddingStart = 12.dp,
                    paddingEnd = 12.dp,
                    paddingTop = 6.dp,
                    paddingBottom = 6.dp,
                    borderColor = Color.Black,
                    borderWidth = 1.dp, // Grosor del borde
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.Center)
                )
                CurvedBorderText(
                    text = "b",
                    textColor = Color.White,
                    backgroundColor = colorResource(id = R.color.azulunicauca),
                    fontSize = 14.sp,
                    paddingStart = 12.dp,
                    paddingEnd = 12.dp,
                    paddingTop = 6.dp,
                    paddingBottom = 6.dp,
                    borderColor = Color.Black,
                    borderWidth = 1.dp, // Grosor del borde
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.Center)
                )
                CurvedBorderText(
                    text = "c",
                    textColor = Color.White,
                    backgroundColor = colorResource(id = R.color.azulunicauca),
                    fontSize = 14.sp,
                    paddingStart = 12.dp,
                    paddingEnd = 12.dp,
                    paddingTop = 6.dp,
                    paddingBottom = 6.dp,
                    borderColor = Color.Black,
                    borderWidth = 1.dp, // Grosor del borde
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.Center)
                )
                CurvedBorderText(
                    text = "ea",
                    textColor = Color.White,
                    backgroundColor = colorResource(id = R.color.azulunicauca),
                    fontSize = 14.sp,
                    paddingStart = 12.dp,
                    paddingEnd = 12.dp,
                    paddingTop = 6.dp,
                    paddingBottom = 6.dp,
                    borderColor = Color.Black,
                    borderWidth = 1.dp, // Grosor del borde
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentSize(Alignment.Center)
                )
            }

            while (true) {
                val xrold = currentXr

                // Evitar división por cero
                val denominator = currentFl - currentFu
                if (abs(denominator) < 1e-10) {
                    Toast.makeText(
                        context,
                        "Error: División por cero detectada",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                // Calcula el nuevo punto usando la fórmula de falsa posición
                try {
                    currentXr = currentXu - (currentFu * (currentXl - currentXu) / denominator)
                } catch (e: Exception) {
                    Log.e("RegularFalsi", "Error al calcular c", e)
                    Toast.makeText(
                        context,
                        "Error en el cálculo: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
                val fr = Metodo(a = currentXr, f = f)
                iter++

                // Calcula el error relativo si xr no es cero
                if (currentXr != 0.0) {
                    ea = abs((currentXr - xrold) / currentXr) * 100
                }
                // Función segura para formatear números
                fun formatNumber(value: Double): String {
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

                val test = currentFl * fr

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
                        text = "$iter",
                        textColor = Color.Black,
                        backgroundColor = colorResource(id = R.color.grisunicauca),
                        fontSize = 10.sp,
                        paddingStart = 6.dp,
                        paddingEnd = 6.dp,
                        paddingTop = 6.dp,
                        paddingBottom = 6.dp,
                        borderColor = Color.Black,
                        borderWidth = 1.dp,
                        modifier = Modifier
                            .weight(0.3f)
                            .wrapContentSize(Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        CurvedBorderText(
                            text = formatNumber(currentXl),
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
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        CurvedBorderText(
                            text = formatNumber(currentXu),
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
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        CurvedBorderText(
                            text = formatNumber(currentXr),
                            textColor = Color.Black, // Color del texto personalizado
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
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        CurvedBorderText(
                            text = formatNumber(ea),
                            textColor = Color.Black, // Color del texto personalizado
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
                }

                when {
                    test < 0 -> {
                        currentXu = currentXr
                        currentFu = fr
                        iu = 0
                        il++
                        if (il >= 2) {
                            currentFl /= 2.0
                        }
                    }

                    test > 0 -> {
                        currentXl = currentXr
                        currentFl = fr
                        il = 0
                        iu++
                        if (iu >= 2) {
                            currentFu /= 2.0
                        }
                    }

                    else -> {
                        ea = 0.0
                    }
                }

                if (ea <= epsilon || iter >= MaxIter) {
                    // Muestra un mensaje emergente con la raíz encontrada
                    Toast.makeText(
                        context,
                        "La raíz de la función $f es: ${formatNumber(currentXr)} después de $iter iteraciones",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }

            }

        }
    }
}
//