package com.example.mathsus_ia.ui.features.nav_menu_bisection

import androidx.compose.foundation.layout.Box
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
import com.example.mathsus_ia.ui.features.InfoFormula
import com.example.mathsus_ia.ui.features.InfoKeyValue
import com.example.mathsus_ia.ui.features.InfoParagraph
import com.example.mathsus_ia.ui.features.InfoSectionTitle
import com.example.mathsus_ia.ui.features.InfoTitle
import com.example.mathsus_ia.ui.features.MethodInfoContent
import com.example.mathsus.ui.features.nav_menu_bisection.DestinosBisection
import com.example.mathsus.ui.features.nav_menu_bisection.DrawerBisection
import com.example.mathsus.ui.features.nav_menu_secante.TopBar

@Composable
fun InformationBisection(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DestinosBisection.Home, DestinosBisection.CalculateBisectionScreen, DestinosBisection.StepBisectionScreen, DestinosBisection.InfoBisectionScreen, DestinosBisection.ExcersiceBisectionScreen)
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = { ModalDrawerSheet { DrawerBisection(items, navController) } }) {
        Scaffold(topBar = { TopBar("Información · Bisección", scope, drawerState) }, bottomBar = { BottomNavBarBisection(navController) }) { padding ->
            Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) { Column(Modifier.padding(padding)) { InfoBisection() } }
        }
    }
}

@Composable
fun InfoBisection() = MethodInfoContent {
    InfoTitle("Método de Bisección", "Un método seguro para localizar una raíz en un intervalo.")
    InfoParagraph("Si f es continua en [a,b] y f(a) y f(b) tienen signos opuestos, el teorema del valor intermedio garantiza al menos una raíz r dentro del intervalo. Bisección conserva esa garantía y reduce el intervalo a la mitad en cada iteración.")
    InfoFormula("f(a)\\,f(b)<0", "Condición inicial: debe existir un cambio de signo")
    InfoSectionTitle("Cómo se calcula")
    InfoParagraph("Se toma el punto medio m. Si f(m)=0, se encontró la raíz. Si f(a) y f(m) tienen signos opuestos, el nuevo intervalo es [a,m]; de lo contrario, es [m,b].")
    InfoFormula("m=\\frac{a+b}{2}", "Punto medio del intervalo")
    InfoFormula("e_m\\leq\\frac{b-a}{2}", "Cota del error absoluto")
    InfoSectionTitle("Ventajas y límites")
    InfoParagraph("Es sencillo, estable y converge cuando se cumplen la continuidad y el cambio de signo. Su desventaja es que normalmente converge más despacio que Newton-Raphson o Secante. Un intervalo puede contener más de una raíz; Bisección localiza una de ellas, no todas.")
    InfoSectionTitle("Ejemplo")
    InfoParagraph("Para f(x)=x²−2 en [1,2], f(1)=−1 y f(2)=2, por lo que existe una raíz. Al repetir el proceso, el intervalo se aproxima a √2≈1.4142.")
    InfoFormula("f(x)=x^2-2\\,,\\quad r=\\sqrt{2}\\approx1.4142")
    InfoSectionTitle("Resumen")
    InfoKeyValue("Datos", "f(x), a, b y tolerancia")
    InfoKeyValue("Requisito", "f(a)·f(b)<0")
    InfoKeyValue("Convergencia", "Garantizada bajo las hipótesis")
}
