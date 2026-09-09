package com.example.mathsus.ui.features.nav_menu_secante

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.mathsus_ia.ui.features.BottomNavBarSecante
import com.example.mathsus_ia.ui.features.InfoFormula
import com.example.mathsus_ia.ui.features.InfoKeyValue
import com.example.mathsus_ia.ui.features.InfoParagraph
import com.example.mathsus_ia.ui.features.InfoSectionTitle
import com.example.mathsus_ia.ui.features.InfoTitle
import com.example.mathsus_ia.ui.features.MethodInfoContent
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun InformationSecante(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosInfoSecante.Pantalla1, DestinosInfoSecante.Pantalla2, DestinosInfoSecante.Pantalla3, DestinosInfoSecante.Pantalla4)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { Drawer(items, navController) } }) {
        Scaffold(topBar = { TopBar("Información · Secante", scope, drawerState) }, bottomBar = { BottomNavBarSecante(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) { InfoSecante() }
        }
    }
}

@Composable
fun InfoSecante() = MethodInfoContent {
    InfoTitle("Método de la Secante", "Aproxima la derivada usando dos puntos y no requiere f'(x).")
    InfoParagraph("El método de la Secante reemplaza la derivada de Newton por la pendiente de la recta que pasa por (xₙ₋₁,f(xₙ₋₁)) y (xₙ,f(xₙ)). Por eso necesita dos aproximaciones iniciales.")
    InfoFormula("x_{n+1}=x_n-f(x_n)\\frac{x_n-x_{n-1}}{f(x_n)-f(x_{n-1})}", "Iteración de la Secante")
    InfoSectionTitle("Interpretación geométrica")
    InfoParagraph("La recta secante corta el eje x en xₙ₊₁. Si los puntos se acercan a una raíz y el denominador no es pequeño, el método suele converger más rápido que Bisección, aunque no ofrece la misma garantía de convergencia.")
    InfoSectionTitle("Precauciones")
    InfoParagraph("El denominador f(xₙ)−f(xₙ₋₁) no puede ser cero ni demasiado pequeño. Tampoco deben aceptarse valores no finitos. MATHSUS detiene el cálculo y muestra un error en esos casos.")
    InfoSectionTitle("Ejemplo")
    InfoParagraph("Para f(x)=x²−2 con x₀=1 y x₁=2, las aproximaciones sucesivas se acercan a √2≈1.4142 sin calcular explícitamente la derivada.")
    InfoFormula("f(x)=x^2-2\\,,\\quad x_0=1\\,,\\quad x_1=2\\,,\\quad x_n\\to\\sqrt{2}")
    Image(painterResource(R.drawable.graph_secante), "Interpretación geométrica del método de la Secante", Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)
    InfoSectionTitle("Resumen")
    InfoKeyValue("Datos", "f(x), x₀, x₁ y tolerancia")
    InfoKeyValue("Ventaja", "No requiere derivada")
    InfoKeyValue("Precaución", "Evitar denominador cero")
}
