package com.example.mathsus_ia.ui.features.nav_menu_newton

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
import com.example.mathsus_ia.ui.features.BottomNavBarNewton
import com.example.mathsus_ia.ui.features.ExerciseCard
import com.example.mathsus_ia.ui.features.PracticeContent
import com.example.mathsus_ia.ui.features.PracticeIntro
import com.example.mathsus_ia.ui.features.PracticeNote
import com.example.mathsus.ui.features.nav_menu_secante.TopBar

@Composable
fun ExerciseNewton(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosNewton.Home, DestinosNewton.CalculateNewtonScreen, DestinosNewton.StepNewtonScreen, DestinosNewton.InfoNewtonScreen, DestinosNewton.ExcersiceNewtonScreen)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { DrawerNewton(items, navController) } }) {
        Scaffold(topBar = { TopBar("Práctica · Newton-Raphson", scope, drawerState) }, bottomBar = { BottomNavBarNewton(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) { ExerNewton() }
        }
    }
}

@Composable
fun ExerNewton() = PracticeContent {
    PracticeIntro("Práctica de Newton-Raphson", "Usa una aproximación inicial y observa cómo influye la derivada.")
    ExerciseCard(1, "Convergencia rápida", "Usa x₀=1 y registra cuatro iteraciones. Comprueba que las aproximaciones se acercan a √2.", "f(x)=x^2-2")
    ExerciseCard(2, "Polinomio clásico", "Usa x₀=2, ejecuta hasta que el error sea menor que 10⁻⁵ y verifica el resultado.", "f(x)=x^3-2x-5")
    ExerciseCard(3, "Dos puntos iniciales", "Repite con x₀=0.5 y x₀=3. Compara la cantidad de iteraciones y el comportamiento observado.", "f(x)=\\cos(x)-x")
    ExerciseCard(4, "Derivada casi nula", "Prueba x₀=0 y explica qué ocurre cuando la derivada es cero. La aplicación debe mostrar un error, no una raíz.", "f(x)=x^3+1")
    ExerciseCard(5, "Sistema de Kepler", "Para ε=0.5, encuentra y para x=1 radian usando Newton. Define f(y)=y−ε sen(y)−x.", "f(y)=y-0.5\\sin(y)-1")
    PracticeNote("Newton-Raphson requiere que introduzcas también la derivada correcta. Usa radianes en las funciones trigonométricas.")
}
