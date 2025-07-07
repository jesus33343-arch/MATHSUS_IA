package com.example.mathsus.ui.methods.newtonMethod

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import com.example.mathsus_ia.ui.methods.Derivada
import com.example.mathsus_ia.ui.methods.Metodo
import org.mariuszgromada.math.mxparser.mathcollection.MathFunctions.abs

@SuppressLint("DefaultLocale")
@Composable
fun NewtonRaphson(
    x: Double,
    f: String,
    error: Double
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .width(380.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .width(IntrinsicSize.Min)
                    .horizontalScroll(rememberScrollState())
                    .background(
                        shape = RoundedCornerShape(4.dp),
                        color = colorResource(id = R.color.grisunicauca)
                    )
            ){
                CurvedBorderText(
                    text = "k",
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
                        .weight(0.5f)
                        .wrapContentSize(Alignment.Center)
                )
                CurvedBorderText(
                    text = "xk",
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
                    text = "f(xk)",
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
            var k = 0 //contador
            var x_k = x

            var fx_k = Metodo(a = x_k, f = f)

            for (iteration in 0..200) {

                val dfx_k = Derivada(a = x_k, f = f)

                if (abs(dfx_k) < error) {
                    Toast.makeText(context, "Derivada pequeña", Toast.LENGTH_SHORT).show()
                } else {

                    val d = (Metodo(a = x_k, f = f) / Derivada(a = x_k, f = f))
                    val x_kPlus1 = x_k - d
                    val fx_kPlus1 = Metodo(a = x_kPlus1, f = f)
                    val roundx_k = String.format("%.4f", x_k)
                    val roundfx_k = String.format("%.4e", fx_k)
                    val parts = roundfx_k.split("e")
                    val coefficient = parts[0].toDouble()
                    val exponent = parts[1].toInt()
                    // Quitar ceros adicionales si es necesario
                    val trimmedCoefficient = if (coefficient % 1 == 0.0) {
                        coefficient.toInt().toString()
                    } else {
                        parts[0].replace(Regex("0*$"), "")
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
                            text = "$k",
                            textColor = Color.Black, // Color del texto personalizado
                            backgroundColor = colorResource(id = R.color.grisunicauca),
                            fontSize = 10.sp,
                            paddingStart = 6.dp,
                            paddingEnd = 6.dp,
                            paddingTop = 6.dp,
                            paddingBottom = 6.dp,
                            borderColor = Color.Black,
                            borderWidth = 1.dp, // Grosor del borde
                            modifier = Modifier
                                .weight(0.5f)
                                .wrapContentSize(Alignment.Center)
                        )
                        CurvedBorderText(
                            text = roundx_k,
                            textColor = Color.Black, // Color del texto personalizado
                            backgroundColor = colorResource(id = R.color.grisunicauca),
                            fontSize = 10.sp,
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
                            text = "$trimmedCoefficient * 10^$exponent ",
                            textColor = Color.Black, // Color del texto personalizado
                            backgroundColor = colorResource(id = R.color.grisunicauca),
                            fontSize = 10.sp,
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
                    if (abs(d) < 0.00001) {
                        Toast.makeText(context, "Converge", Toast.LENGTH_SHORT).show()
                        break
                    }
                    fx_k = fx_kPlus1
                    k++
                    x_k = x_kPlus1
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "El polinomio $f tiene una raiz real en $x_k")
        }
    }
}


