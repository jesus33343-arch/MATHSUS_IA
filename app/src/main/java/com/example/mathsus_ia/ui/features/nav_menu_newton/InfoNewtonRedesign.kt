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
import com.example.mathsus_ia.ui.features.InfoFormula
import com.example.mathsus_ia.ui.features.InfoKeyValue
import com.example.mathsus_ia.ui.features.InfoParagraph
import com.example.mathsus_ia.ui.features.InfoSectionTitle
import com.example.mathsus_ia.ui.features.InfoTitle
import com.example.mathsus_ia.ui.features.MethodInfoContent
import com.example.mathsus.ui.features.nav_menu_secante.TopBar

@Composable
fun InformationNewton(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosNewton.Home, DestinosNewton.CalculateNewtonScreen, DestinosNewton.StepNewtonScreen, DestinosNewton.InfoNewtonScreen, DestinosNewton.ExcersiceNewtonScreen)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { DrawerNewton(items, navController) } }) {
        Scaffold(topBar = { TopBar("Información · Newton-Raphson", scope, drawerState) }, bottomBar = { BottomNavBarNewton(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) { InfoNewton() }
        }
    }
}

@Composable
fun InfoNewton() = MethodInfoContent {
    InfoTitle("Método de Newton-Raphson", "Un método rápido que usa la derivada de la función.")
    InfoParagraph("Newton-Raphson aproxima la gráfica de f mediante la recta tangente en la aproximación actual xₙ. El corte de esa tangente con el eje x produce la siguiente aproximación.")
    InfoFormula("x_{n+1}=x_n-\\frac{f(x_n)}{f'(x_n)}", "Iteración de Newton-Raphson")
    InfoSectionTitle("Interpretación")
    InfoParagraph("Se necesita una aproximación inicial x₀ y una función derivable. Cuando la aproximación está cerca de una raíz simple y la derivada no es cercana a cero, el método suele converger muy rápido, de forma cuadrática.")
    InfoFormula("l(x)=f(x_n)+f'(x_n)(x-x_n)")
    InfoParagraph("La fórmula se obtiene al imponer l(x)=0. Una derivada nula o casi nula puede producir pasos enormes o impedir el cálculo; MATHSUS informa esa situación en lugar de aceptar una raíz falsa.")
    InfoSectionTitle("Ejemplo")
    InfoParagraph("Para f(x)=x²−2 y x₀=1, f'(x)=2x. Las iteraciones son x₁=1.5, x₂≈1.4167 y continúan hacia √2≈1.4142.")
    InfoFormula("x_{n+1}=\\frac{1}{2}\\left(x_n+\\frac{2}{x_n}\\right)")
    InfoSectionTitle("Resumen")
    InfoKeyValue("Datos", "f(x), f'(x), x₀ y tolerancia")
    InfoKeyValue("Ventaja", "Convergencia rápida cerca de la raíz")
    InfoKeyValue("Precaución", "f'(xₙ) no debe ser cero")
}
