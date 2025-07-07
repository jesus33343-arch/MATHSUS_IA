package com.example.mathsus_ia.ui.methods

import androidx.compose.runtime.Composable
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function

@Composable
fun Metodo(a: Double, f: String): Double {
    val f = Function("f", f, "x")
    val fa = org.mariuszgromada.math.mxparser.Expression("f(${a})", f).calculate()
    return fa
}

@Composable
fun Derivada(a: Double, f: String): Double {
    val df = Expression("der($f, x, ${a})").calculate()
    return df
}

fun calcularFuncion(a: Double, f: String): Double {
    val funcion = Function("f", f, "x")
    val expresion = org.mariuszgromada.math.mxparser.Expression("f($a)", funcion)
    return expresion.calculate()
}