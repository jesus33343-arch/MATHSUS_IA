package com.example.mathsus.ui.features.nav_menu_bisection

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
import com.example.mathsus_ia.ui.features.BottomNavBarBisection
import com.example.mathsus_ia.ui.features.ExerciseCard
import com.example.mathsus_ia.ui.features.PracticeContent
import com.example.mathsus_ia.ui.features.PracticeIntro
import com.example.mathsus_ia.ui.features.PracticeNote

@Composable
fun ExerciseBisection(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosBisection.Home, DestinosBisection.CalculateBisectionScreen, DestinosBisection.StepBisectionScreen, DestinosBisection.InfoBisectionScreen, DestinosBisection.ExcersiceBisectionScreen)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { DrawerBisection(items, navController) } }) {
        Scaffold(topBar = { com.example.mathsus.ui.features.nav_menu_secante.TopBar("Práctica · Bisección", scope, drawerState) }, bottomBar = { BottomNavBarBisection(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) { Column(Modifier.padding(padding)) { ExerBisection() } }
        }
    }
}

@Composable
fun ExerBisection() = PracticeContent {
    PracticeIntro("Práctica de Bisección", "Todos los intervalos iniciales fueron revisados para conservar un cambio de signo.")
    ExerciseCard(1, "Raíz de un polinomio", "Usa [1,2] y una tolerancia de 10⁻⁴. Registra el intervalo y el error en cada iteración.", "f(x)=x^3-2x-5")
    ExerciseCard(2, "Raíz cuadrada", "Encuentra √2 con [1,2] y tolerancia 10⁻⁵. Compara la respuesta con el valor conocido.", "f(x)=x^2-2")
    ExerciseCard(3, "Función trigonométrica", "Encuentra una raíz en [4.4,4.6] con tolerancia 10⁻⁴. Trabaja con x en radianes; este intervalo evita la asíntota de la tangente.", "f(x)=\\tan(x)-x")
    ExerciseCard(4, "Ecuación exponencial", "Encuentra la raíz en [0,1] y comprueba que el valor obtenido hace |f(r)| pequeño.", "f(x)=e^x-3x")
    ExerciseCard(5, "Caso sin cambio de signo", "Prueba [1,2] para esta función y explica por qué el método debe rechazar el intervalo. Luego prueba [0,2].", "f(x)=x^2+1")
    PracticeNote("Sugerencia: copia la tabla de resultados y verifica siempre que el intervalo inicial cumpla f(a)·f(b)<0.")
}
