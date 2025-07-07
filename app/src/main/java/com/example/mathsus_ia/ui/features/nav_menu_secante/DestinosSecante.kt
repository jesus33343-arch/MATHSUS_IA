package com.example.mathsus.ui.features.nav_menu_secante

import io.github.jesusgurrute.mathsus_ia.R


sealed class DestinosSecante(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Pantalla1: DestinosSecante(R.drawable.ic_home, "Inicio", "splash")
    data object Pantalla2: DestinosSecante(R.drawable.icon_calculadora, "Secante pasoa  paso", "pasoSecante")
    data object Pantalla3: DestinosSecante(R.drawable.ic_teoria, "Teoria", "infoSecante")
    data object Pantalla4: DestinosSecante(R.drawable.ic_ejercicio, "Ejercicios", "exerciseSecante")
}

sealed class DestinosPasoSecante(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Pantalla1: DestinosSecante(R.drawable.ic_home, "Inicio", "splash")
    data object Pantalla2: DestinosSecante(R.drawable.icon_calculadora, "Método de la secante", "secante")
    data object Pantalla3: DestinosSecante(R.drawable.ic_teoria, "Teoria", "infoSecante")
    data object Pantalla4: DestinosSecante(R.drawable.ic_ejercicio, "Ejercicios", "exerciseSecante")
}

sealed class DestinosInfoSecante(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Pantalla1: DestinosSecante(R.drawable.ic_home, "Inicio", "splash")
    data object Pantalla2: DestinosSecante(R.drawable.icon_calculadora, "Método de la secante", "secante")
    data object Pantalla3: DestinosSecante(R.drawable.ic_teoria, "Secante paso a paso", "pasoSecante")
    data object Pantalla4: DestinosSecante(R.drawable.ic_ejercicio, "Ejercicios", "exerciseSecante")
}

sealed class DestinosExcerSecante(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Pantalla1: DestinosSecante(R.drawable.ic_home, "Inicio", "splash")
    data object Pantalla2: DestinosSecante(R.drawable.icon_calculadora, "Método de la secante", "secante")
    data object Pantalla3: DestinosSecante(R.drawable.ic_teoria, "Secante paso a paso", "pasoSecante")
    data object Pantalla4: DestinosSecante(R.drawable.ic_ejercicio, "Teoria", "infoSecante")
}
