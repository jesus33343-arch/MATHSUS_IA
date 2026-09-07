package com.example.mathsus_ia.ui.features.nav_menu_newton

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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun ExerciseNewton(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosNewton.Home,
        DestinosNewton.CalculateNewtonScreen,
        DestinosNewton.StepNewtonScreen,
        DestinosNewton.InfoNewtonScreen
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerNewton(menuItems = navigationItems, navController = navController)
            }
        }
    ) {
        Scaffold(
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
                        ExerNewton()
                    }

                }
            },
            bottomBar = { BottomNavBarNewton(navController = navController) },
        )
    }
}

@Composable
fun ExerNewton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "1. Usando el procedimiento de Newton y una sola corrida de la aplicación, pruebe su código con estos ejemplos:      f(t) = tan t – t con x₀ = 7 y e^t - sqrt(t + 9) con x₀ = 2. Imprima cada iteración y su correspondiente valor de la función.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "2. Aplicar el método de Newton a la ecuación x^3 + 2x^2 + 10x = 20, iniciando con x₀ = 2. Evalúe las adecuadas f(x) y  f´(x) usando multiplicación anidada. Detenga el cálculo cuando dos puntos sucesivos difieran por (1/2)*10^(-5) ó 0.000005 ",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "3. En 1685, John Wallis publicó un libro llamado Algebra, en el que describió un método diseñado por Newton para resolver ecuaciones. En forma ligeramente modificada, este método también lo publicó Joseph Raphson en 1690. Esta forma es la que ahora comúnmente se llama método de Newton o método de Newton-Raphson. Newton mismo analizó el método en 1669 y lo ilustró con la ecuación x^3 – 2x – 5 = 0. Wallis usó el mismo ejemplo. Encuentre una raíz de esta ecuación y así preserva la tradición de que cada estudiante de análisis numérico debía resolver esta venerable ecuación. ",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "4. En mecánica celeste, la ecuación de Kepler es importante. Ésta se escribe como x = y – ε sen(y), en el que x la anomalía media de un planeta y su excentricidad anómala y ε la excentricidad de su órbita. Tomando ε = 0.9, construya una tabla de y para 30 valores igualmente espaciados de x en el intervalo 0 ≤ x ≤ π. Use el método de Newton para obtener cada valor de y. La y correspondiente a una x se puede usar como el punto de partida para la iteración cuando x cambia ligeramente.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "5. En el método de Newton, avanzamos en cada paso de un punto dado x a un nuevo punto x – h, donde h = f (x)/f´ (x). Un refinamiento que es fácilmente programado es este: si |f (x – h)| no es más pequeño que |f(x)|, entonces rechace este valor de h y use en su lugar h/2. Pruebe este refinamiento.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "6. Calcular una raíz de la ecuación x^3 = x^2 + x + 1, usando el método de Newton. Tenga cuidado al elegir un valor inicial conveniente.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "7. Encuentre la raíz de la ecuación 5(3x^4 – 6x^2 + 1) = 2(3x^5 – 5x^3) que está en el intervalo [0, 1] usando el método de Newton.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "8. Calcular e imprimir ocho pasos del método de Newton para determinar una raíz positiva.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.exer_cero_newton), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "9. Le gustaría ver al número 0.55887 766 como resultado de un cálculo? Tome tres pasos en el método de Newton en 10 + x^3 – 12 cos x = 0 iniciando con x₀ = 1.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "10. Encontrar una raíz de la ecuación e^(x^2) = cos x + 1 en [0, 4]. ¿Qué pasa en el método de Newton si iniciamos con x₀ = 0 o con x₀ = 1?",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "11. Encuentre la raíz de la ecuación (1/2) x^2 + x + 1 e^x = 0 usando el método de Newton, iniciando x₀= 1 y considerando convergencia lenta.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "12. Usando f(x) = x^5– 9x^4 – x^3 + 17x^2 – 8x – 8 y x₀ = 0, estudie y explique el comportamiento del método de Newton. Sugerencia: las iteraciones son inicialmente cíclicas.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "13. Encuentre el cero de la función f (x)= x – tan x que está más cerca de 99 (radianes) por el método de Bisección y por el método de Newton. Sugerencia: se necesitan valores iniciales extremadamente exactos para esta función. Use la aplicación para construir una tabla de valores de f(x) alrededor de 99 para determinar la naturaleza de esta función.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "14. Usando el método de Bisección, encuentre la raíz positiva de 2x(1 + x^2)^(–1) = arctan x. Usando la raíz como x₀, aplique el método de Newton para la función arctan x. Interprete los resultados.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "15. Resuelva este par de ecuaciones simultáneas no lineales pero primero elimine y y después resuelva la ecuación resultante en x con el método de Newton. Inicie con el valor inicial x₀ = 1.0.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.exer_uno_newton), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

    }


}
