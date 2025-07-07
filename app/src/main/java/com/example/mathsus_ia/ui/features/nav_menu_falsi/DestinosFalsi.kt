package com.example.mathsus.ui.features.nav_menu_falsi

import io.github.jesusgurrute.mathsus_ia.R


sealed class DestinosFalsi(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Home: DestinosFalsi(R.drawable.ic_home, "Inicio", "splash")
    data object CalculateFalsiScreen: DestinosFalsi(R.drawable.icon_calculadora, "Método", "falsi")
    data object StepFalsiScreen: DestinosFalsi(R.drawable.icon_calculadora, "Regular Falsi paso a  paso", "pasoFalsi")
    data object InfoFalsiScreen: DestinosFalsi(R.drawable.ic_ejercicio, "Teoria", "infoFalsi")
    data object ExcersiceFalsiScreen: DestinosFalsi(R.drawable.ic_ejercicio, "Ejercicios", "exerciseFalsi")
}