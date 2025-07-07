package com.example.mathsus.ui.features.nav_menu_bisection

data class ResultadoBisection(
    val iteracion: Int,
    var a: Double,
    val b: Double,
    val c: Double,
    val previousA: Double  // Agregamos este campo
) {

}