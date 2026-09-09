package com.example.mathsus.ui.features.nav_menu_secante

import androidx.compose.foundation.Image
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.features.BottomNavBarSecante

@Composable
fun LegacyExerciseSecante(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosExcerSecante.Pantalla1,
        DestinosExcerSecante.Pantalla2,
        DestinosExcerSecante.Pantalla3,
        DestinosExcerSecante.Pantalla4
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Drawer(menuItems = navigationItems, navController = navController)
            }
        }
    ) {
        androidx.compose.material3.Scaffold(
            topBar = { TopBar("Ejercicios de cómputo", scope, drawerState) },
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
                        LegacyExerSecante()
                    }
                }
            },
            bottomBar = { BottomNavBarSecante(navController = navController) },

            )
    }
}
@Composable
fun LegacyExerSecante() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        androidx.compose.material3.Text(
            text = "1. Si usamos el método de la Secante en f(x) = x^3 – 2x + 2 iniciando con x₀ = 0 y x1 = 1, ¿a qué es igual x2?",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "2. Si el método de la Secante se usa en f(x) = x^5 + x^3 + 3 y si x_(n–2) = 0 y x_(n–1) = 0, ¿a qué es igual x_(n–1)?",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "3. Usando el método de Bisección, el método de Newton y el método de la Secante, encuentre la raíz positiva más grande correcta a tres lugares decimales de x^3 – 5x + 3 = 0. (Todas raíces están en [–3, +3]).",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "4. Use el método de la Secante para encontrar el cero cerca de –0.5 de f(x) = e^x – 3x^2. Esta función también tiene un cero cerca de 4. Encuentre este cero positivo usando el método de Newton.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "5. Comparar el método de la Secante con el método de Newton para determinar una raíz de cada función",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.exer_cero_secante), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "Use el valor x1 del método de Newton como el segundo punto de inicio para el método de la Secante. Imprima cada iteración para los dos métodos",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "6. Encontrar la raíz de f (x) = x^3 + 2x^2 + 10x – 20 usando el método de la Secante con valores iniciales x0 = 2 y x1 = – 1. Déjelo correr a lo más 20 pasos e incluya también una prueba de parada. Compare el número de pasos necesario aquí con el número de pasos necesarios con el método de Newton. ¿La convergencia es cuadrática?",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "6. Encontrar la raíz de f (x) = x^3 + 2x^2 + 10x – 20 usando el método de la Secante con valores iniciales x0 = 2 y x1 = – 1. Déjelo correr a lo más 20 pasos e incluya también una prueba de parada. Compare el número de pasos necesario aquí con el número de pasos necesarios con el método de Newton. ¿La convergencia es cuadrática?",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "7. Pruebe el método de la Secante con el conjunto de funciones f(x) = 2e^(–k)x + 1 – 3e^(–kx) para k = 1, 2, 3, . . . , 10. Use los puntos de inicio 0 y 1 en cada caso",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "8. Un ejemplo de Wilkinson [1963] muestra que diminutas alteraciones en los coeficientes de un polinomio pueden tener efectos masivos en las raíces. Sea",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.exer_uno_secante), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "que se conoce como el polinomio de Wilkinson. Los ceros de f son, por supuesto, los enteros 1, 2, . . . , 20. Trate de determinar qué le ocurre al cero r = 20 cuando la función se altera como f(x) – 10^(–8)x^(19). Sugerencia: el método de la Secante en doble precisión localizará un cero en el intervalo [20, 21].",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "9. Encuentre el cero real del polinomio p(x) = x^5 + x^3 + 3.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "10. Encuentre los puntos fijos para cada una de las siguientes funciones:",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.exer_dos_secante), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.Text(
            text = "11. Para la ecuación no lineal f(x) = x^2 – x – 2 = 0 con raíces 1 y 2, escriba cuatro problemas de punto fijo x = g(x) que sean equivalentes. Trace la gráfica de todas y muestre que todas intersecan la recta x – y. También, trace la gráfica de los pasos de la convergencia de cada una de estas iteraciones de punto fijo para diferentes valores iniciales x(0). Muestre que el comportamiento de estos esquemas de punto fijo puede variar fuertemente: convergencia lenta, convergencia rápida y divergencia.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
