package com.example.mathsus.ui.features.nav_menu_falsi

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
import com.example.mathsus_ia.ui.features.BottomNavBarFalsi
import com.example.mathsus_ia.ui.features.ExerciseCard
import com.example.mathsus_ia.ui.features.PracticeContent
import com.example.mathsus_ia.ui.features.PracticeIntro
import com.example.mathsus_ia.ui.features.PracticeNote
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus.ui.features.nav_menu_falsi.DestinosFalsi
import com.example.mathsus.ui.features.nav_menu_falsi.DrawerFalsi

@Composable
fun ExerciseFalsi(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosFalsi.Home, DestinosFalsi.CalculateFalsiScreen, DestinosFalsi.StepFalsiScreen, DestinosFalsi.InfoFalsiScreen, DestinosFalsi.ExcersiceFalsiScreen)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { DrawerFalsi(items, navController) } }) {
        Scaffold(topBar = { TopBar("Práctica · Regular Falsi", scope, drawerState) }, bottomBar = { BottomNavBarFalsi(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) { ExerFalsi() }
        }
    }
}

@Composable
fun ExerFalsi() = PracticeContent {
    PracticeIntro("Práctica de Regular Falsi", "Ejercicios para comparar la secante con la conservación del cambio de signo.")
    ExerciseCard(1, "Primera aproximación", "Usa [1,2], calcula al menos cinco iteraciones y registra c y el error aproximado.", "f(x)=x^2-2")
    ExerciseCard(2, "Polinomio cúbico", "Encuentra una raíz en [1,2] con tolerancia 10⁻⁵ y verifica el residuo final.", "f(x)=x^3-2x-5")
    ExerciseCard(3, "Exponencial", "Encuentra la raíz en [0,1]. Compara el número de iteraciones con Bisección usando la misma tolerancia.", "f(x)=e^x-3x")
    ExerciseCard(4, "Actualización del intervalo", "Usa [2,3]. Explica en cada paso cuál extremo se reemplaza y por qué se conserva f(a)·f(b)<0.", "f(x)=\\ln(x)-1")
    ExerciseCard(5, "Caso difícil", "Usa [0,1] y observa si un extremo permanece fijo. Repite con la actualización Illinois de la aplicación.", "f(x)=x^10-1")
    PracticeNote("Regular Falsi necesita un intervalo válido. No uses puntos iniciales que no tengan cambio de signo.")
}
