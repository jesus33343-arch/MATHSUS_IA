package com.example.mathsus.ui.features.nav_menu_secante

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.mathsus_ia.ui.features.BottomNavBarSecante
import com.example.mathsus_ia.ui.features.ExerciseCard
import com.example.mathsus_ia.ui.features.PracticeContent
import com.example.mathsus_ia.ui.features.PracticeIntro
import com.example.mathsus_ia.ui.features.PracticeNote

@Composable
fun ExerciseSecante(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosSecante.Pantalla1, DestinosSecante.Pantalla2, DestinosSecante.Pantalla3, DestinosSecante.Pantalla4)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { Drawer(items, navController) } }) {
        Scaffold(topBar = { TopBar("Práctica · Secante", scope, drawerState) }, bottomBar = { BottomNavBarSecante(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) { ExerSecante() }
        }
    }
}

@Composable
fun ExerSecante() = PracticeContent {
    PracticeIntro("Práctica de la Secante", "Dos puntos iniciales, sin derivada explícita.")
    ExerciseCard(1, "Primera iteración", "Calcula x₂ manualmente y luego compruébalo en la aplicación.", "f(x)=x^3-2x+2\\,,\\quad x_0=0\\,,\\quad x_1=1")
    ExerciseCard(2, "Raíz de un polinomio", "Usa x₀=1 y x₁=2. Ejecuta hasta una tolerancia de 10⁻⁵ y compara con √2.", "f(x)=x^2-2")
    ExerciseCard(3, "Comparación con Newton", "Encuentra una raíz con x₀=2 y x₁=1. Compara el número de iteraciones con Newton usando x₀=2.", "f(x)=x^3-2x-5")
    ExerciseCard(4, "Dos raíces", "Busca la raíz cercana a −0.5 con x₀=−1 y x₁=0. Luego busca la raíz positiva con x₀=3 y x₁=4.", "f(x)=e^x-3x^2")
    ExerciseCard(5, "Denominador cero", "Prueba x₀=−1 y x₁=1. Explica por qué el método debe detenerse cuando los valores de la función coinciden.", "f(x)=x^2+1")
    PracticeNote("La Secante no necesita derivada, pero sí dos valores iniciales distintos y un denominador f(xₙ)−f(xₙ₋₁) diferente de cero.")
}
