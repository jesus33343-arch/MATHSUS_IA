package com.example.mathsus.ui.features.nav_menu_falsi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus_ia.ui.features.BottomNavBarFalsi

@Composable
fun ExerciseFalsi(navController: NavHostController){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosFalsi.Home,
        DestinosFalsi.CalculateFalsiScreen,
        DestinosFalsi.StepFalsiScreen,
        DestinosFalsi.InfoFalsiScreen
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerFalsi(menuItems = navigationItems, navController = navController)
            }

        }
    ) {
        Scaffold(
            topBar = { TopBar("Ejercicio de cómputo", scope, drawerState) },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        ExerFalsi()
                    }
                }
            },
            bottomBar = { BottomNavBarFalsi(navController = navController) },
        )
    }
}

@Composable
fun ExerFalsi() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "1. Usando el método de Falsa Posición, determine el punto de intersección de las curvas dadas por y = x^3 – 2x + 1 y y = x^2",
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Text(
                text = "2 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Determine las raíces reales de f(x) = -0.5x² + 2.5x + 4.5:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre líneas

            Text(
                text = "a) Gráficamente",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "b) Empleando la fórmula cuadrática",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "c) Usando el método de Bisección con tres iteraciones para determinar la raíz más grande. " +
                        "Emplee como valores iniciales a = 5 y b = 10. " +
                        "Calcule el error estimado εₐ y el error verdadero εₜ para cada iteración.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
        }

        Column {
            Text(
                text = "3 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Determine las raíces reales de f(x) = -25182x - 90x² + 44x³ - 8x⁴ + 0.7x⁵:",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre líneas

            Text(
                text = "a) Gráficamente",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "b) Usando el método de Bisección para localizar la raíz más grande con εₛ = 10%. " +
                        "Utilice como valores iniciales a = 0.5 y b = 1.0.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "c) Realice el mismo cálculo que en b), pero con el método de la Falsa Posición y εₛ = 0.2%.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
        }

        Column {
            Text(
                text = "4 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Localice la primera raíz no trivial de sen(x) = x², donde x está en radianes. " +
                        "Use una técnica gráfica y Bisección con un intervalo inicial de 0.5 a 1. " +
                        "Haga el cálculo hasta que εₐ sea menor que εₛ = 2%. " +
                        "Realice también una prueba de error sustituyendo la respuesta final en la ecuación original.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column {
            // Sección 5.6
            Text(
                text = "5 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Determine la raíz real de ln(x²) = 0.7:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "a) Gráficamente",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "b) Empleando tres iteraciones en el método de Bisección con los valores iniciales a = 0.5 y b = 2.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "c) Usando tres iteraciones del método de la Falsa Posición, con los mismos valores iniciales de b).",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre secciones

            // Sección 6
            Text(
                text = "6 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Determine la raíz real de f(x) = (0.8 - 0.3x)/x:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "a) Analíticamente",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "b) Gráficamente",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "c) Empleando tres iteraciones en el método de la Falsa Posición, con valores iniciales de 1 a 3. " +
                        "Calcule el error aproximado εₐ y el error verdadero εₜ en cada iteración.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre secciones

            // Sección 7
            Text(
                text = "7 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Calcule la raíz cuadrada positiva de 18 usando el método de la Falsa Posición con εₛ = 0.5%. " +
                        "Emplee como valores iniciales a = 4 y b = 5.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column {
            // Sección 8
            Text(
                text = "8 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Encuentre la raíz positiva más pequeña de la función (x está en radianes) x²|cos x| = 5 usando el método de la Falsa Posición. " +
                        "Para localizar el intervalo en donde se encuentra la raíz, grafique primero esta función para valores de x entre 0 y 5. " +
                        "Realice el cálculo hasta que εₐ sea menor que εₛ = 1%. Compruebe su respuesta final sustituyéndola en la función original.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre secciones

            // Sección 9
            Text(
                text = "9 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Encuentre la raíz positiva de f(x) = x⁴ - 8x³ - 35x² + 450x - 1001, utilizando el método de la Falsa Posición. " +
                        "Tome como valores iniciales a = 4.5 y b = 9, y ejecute cinco iteraciones. " +
                        "Calcule los errores tanto aproximado como verdadero, con base en el hecho de que la raíz es 5.60979. " +
                        "Emplee una gráfica para explicar sus resultados y hacer el cálculo dentro de un εₛ = 1.0%.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre secciones

            // Sección 10
            Text(
                text = "10 ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Determine la raíz real de x³.⁵ = 80:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "a) En forma analítica.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "b) Con el método de la Falsa Posición dentro de εₛ = 2.5%. " +
                        "Haga elecciones iniciales de 2.0 a 5.0.",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
        }



    }
}