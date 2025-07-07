package com.example.mathsus.ui.features.nav_menu_secante

// Clase de datos para los resultados
data class ResultadoSecante(
    val iteracion: Int,
    val x0: Double,
    val x1: Double,
    val x2: Double,
    val errorRelativo: Double = 0.0
)
