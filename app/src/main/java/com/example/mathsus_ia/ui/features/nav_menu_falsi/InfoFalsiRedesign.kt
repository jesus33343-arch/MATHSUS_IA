package com.example.mathsus_ia.ui.features.nav_menu_falsi

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
import com.example.mathsus_ia.ui.features.InfoFormula
import com.example.mathsus_ia.ui.features.InfoKeyValue
import com.example.mathsus_ia.ui.features.InfoParagraph
import com.example.mathsus_ia.ui.features.InfoSectionTitle
import com.example.mathsus_ia.ui.features.InfoTitle
import com.example.mathsus_ia.ui.features.MethodInfoContent
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus.ui.features.nav_menu_falsi.DestinosFalsi
import com.example.mathsus.ui.features.nav_menu_falsi.DrawerFalsi

@Composable
fun InformationFalsi(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosFalsi.Home, DestinosFalsi.CalculateFalsiScreen, DestinosFalsi.StepFalsiScreen, DestinosFalsi.InfoFalsiScreen, DestinosFalsi.ExcersiceFalsiScreen)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { DrawerFalsi(items, navController) } }) {
        Scaffold(topBar = { TopBar("Información · Regular Falsi", scope, drawerState) }, bottomBar = { BottomNavBarFalsi(navController) }) { padding ->
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding)) { InfoFalsi() }
        }
    }
}

@Composable
fun InfoFalsi() = MethodInfoContent {
    InfoTitle("Método Regular Falsi", "También llamado método de la falsa posición.")
    InfoParagraph("Regular Falsi combina la seguridad de un intervalo con una aproximación lineal. Parte de [a,b], donde f es continua y existe un cambio de signo. En lugar de usar el punto medio, utiliza la intersección de la secante con el eje x.")
    InfoFormula("f(a)\\,f(b)<0")
    InfoSectionTitle("Fórmula de la falsa posición")
    InfoFormula("c=\\frac{a f(b)-b f(a)}{f(b)-f(a)}", "Intersección de la secante con el eje x")
    InfoParagraph("Después de calcular c, se conserva el subintervalo que mantiene el cambio de signo: si f(a)f(c)<0, se reemplaza b por c; si no, se reemplaza a por c. Si f(c)=0, c es la raíz.")
    InfoSectionTitle("Ventajas y límites")
    InfoParagraph("Puede avanzar más rápido que Bisección cuando la gráfica es favorable y mantiene un intervalo seguro. Sin embargo, un extremo puede quedar fijo durante muchas iteraciones. MATHSUS incorpora la actualización Illinois para reducir ese estancamiento.")
    InfoSectionTitle("Ejemplo")
    InfoParagraph("Para f(x)=x²−2 en [1,2], el cambio de signo permite iniciar el método. Las intersecciones sucesivas se acercan a √2≈1.4142.")
    InfoFormula("f(x)=x^2-2\\,,\\quad c\\to\\sqrt{2}\\approx1.4142")
    InfoSectionTitle("Resumen")
    InfoKeyValue("Datos", "f(x), a, b y tolerancia")
    InfoKeyValue("Requisito", "f(a)·f(b)<0")
    InfoKeyValue("Idea", "Secante + cambio de signo")
}
