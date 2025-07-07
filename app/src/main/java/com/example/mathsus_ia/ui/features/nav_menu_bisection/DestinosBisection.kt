package com.example.mathsus.ui.features.nav_menu_bisection

import io.github.jesusgurrute.mathsus_ia.R

sealed class DestinosBisection(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Home: DestinosBisection(R.drawable.ic_home, "Inicio", "splash")
    data object CalculateBisectionScreen: DestinosBisection(R.drawable.icon_calculadora, "Método", "Bisection")
    data object StepBisectionScreen: DestinosBisection(R.drawable.icon_calculadora, "Bisección paso a  paso", "pasoBisection")
    data object InfoBisectionScreen: DestinosBisection(R.drawable.ic_ejercicio, "Teoria", "infoBisection")
    data object ExcersiceBisectionScreen: DestinosBisection(R.drawable.ic_ejercicio, "Ejercicios", "exerciseBisection")
}
