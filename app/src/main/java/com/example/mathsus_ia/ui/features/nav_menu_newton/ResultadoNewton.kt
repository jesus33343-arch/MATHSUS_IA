package com.example.mathsus_ia.ui.features.nav_menu_newton

data class ResultadoNewton(
    val iteracion: Int,
    val x: Double,
    val fx: Double,
    val dfx: Double,
    val nextX: Double,
    val symbolicDerivative: String
)