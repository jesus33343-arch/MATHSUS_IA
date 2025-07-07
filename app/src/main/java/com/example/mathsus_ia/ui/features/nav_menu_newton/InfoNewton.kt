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
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun InformationNewton(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosNewton.Home,
        DestinosNewton.CalculateNewtonScreen,
        DestinosNewton.StepNewtonScreen,
        DestinosNewton.ExcersiceNewtonScreen
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
            topBar = { TopBar("Interpretación del método", scope, drawerState) },
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
                        InfoNewton()
                    }
                }
            },
            bottomBar = { BottomNavBarNewton(navController = navController) },
        )
    }

}

@Composable
fun InfoNewton() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "En el método de Newton, se supone desde el principio que la función f es derivable. Esto implica que la gráfica de f tiene una pendiente definida en cada punto y, por tanto, una recta tangente única. Ahora permítanos estudiar la siguiente idea simple. En un punto dado (x₀, f(x₀)) en la gráfica de f, hay una tangente, que es una bastante buena aproximación a la curva en la vecindad del punto. Analíticamente, esto significa que la función lineal:",
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_cero_newton), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "está cerca de la función dada f cerca de x₀. En x₀ las dos funciones l y f concuerdan. Tomamos el cero de f como una aproximación del cero de l. El cero de l se encuentra fácilmente:",
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.ec_uno_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Por esto, empezando con el punto x₀ (al que podemos interpretar como una aproximación a la raíz buscada), pasemos a un nuevo punto x1 obtenido de la fórmula anterior.Naturalmente, el proceso \n" +
                    "se puede repetir (iterando) para producir una sucesión de puntos:",
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.ec_dos_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "En condiciones favorables, la sucesión de puntos tenderá a un cero de f.",
            textAlign = TextAlign.Justify
        )

        Text(
            text = "En la siguiente figura se muestra la geometría del método de Newton. La recta y = l(x) es tangente a la curva y = f(x). Ésta interseca el eje x en un punto x1. La pendiente de l(x) es f´(x0).",
            textAlign = TextAlign.Justify
        )


        Image(
            painter = painterResource(id = R.drawable.graph_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Hay otras maneras de interpretar el método de Newton. Suponga de nuevo que x₀ es una aproximación inicial a una raíz de f. Nos preguntamos: ¿qué corrección h se debe sumar a x₀ para obtener la raíz exactamente? Obviamente, queremos",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "f(x₀+h)=0", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Si f es una función suficientemente bien comportada, se tendrá una serie de Taylor en x₀. Así, podríamos escribir")

        Image(
            painter = painterResource(id = R.drawable.ec_tres_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Por supuesto, no es fácil determinar h a partir de esta ecuación. Por tanto, nos rendimos a la expectativa de llegar a la raíz verdadera en un solo paso y buscamos sólo una aproximación para h. Ésta se puede obtener al despreciar los dos primeros términos en la serie",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "f(x₀) + hf´(x₀) = 0", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "La h que resuelve esta no es la h que resuelve f(x₀ + h) = 0, pero es el número que se calcula más fácilmente",
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.ec_cuatro_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Nuestra nueva aproximación es entonces",
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.ec_cinco_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "y el proceso se puede repetir. En retrospectiva, vemos que después de todo no era necesaria la serie de Taylor, ya que sólo usamos los primeros dos términos. En el análisis que después haremos, se ha supuesto que f´´ es continua en una vecindad de la raíz. Esta suposición nos permite calcular los errores del proceso.\n" +
                    " Si el método de Newton se describe en términos de una sucesión x₀,x1, . . . , entonces se aplica la siguiente definición recursiva o inductiva:",
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.ec_seis_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "aturalmente, la pregunta interesante es si",
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.ec_siete_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "donde r es la raíz deseada.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ejemplo 1",
            color = Color.Blue,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Si f(x)= x^3 – x + 1 y x₀ = 1, ¿cuáles son x_1 y x_2 en la iteración de Newton?",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Solución",
            color = Color.Blue,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "De la fórmula básica, x1 = x₀ – f(x₀)/f´(x₀). Ahora f´(x) = 3x^2 – 1, y así f´(1) = 2. También, encontramos f (1) = 1. Por tanto, tenemos x1 = 1-(1/2) = 1/2. De manera similar, obtenemos f (1/2) = (5/8), f´ (1/2) = -1/4 y x_2 = 3 ",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Usando el valor inicial de x como punto de partida, realizamos un máximo de 200 iteraciones del método de Newton. Los procedimientos se deben proporcionar para las funciones externas f(x) y f´(x). El parámetro ε (error) se usa para controlar la convergencia y están relacionados con la exactitud deseada o a la precisión de máquina disponible.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ilustración",
            color = Color.Blue,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ahora mostraremos el método de Newton para localizar una raíz de x^3 + x = 2x^2 + 3. Aplicamos el método a la función f(x) = x^3 – 2x^2 + x – 3, iniciando con x₀ = 3. Por supuesto, f´(x) = 3x^2 – 4x + 1, y estas dos funciones se debe arreglar en forma anidada por eficiencia:",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_ocho_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Para ver con mayor detalle la convergencia rápida del método de Newton usamos aritmética con el doble de la precisión normal en el programa y obtenemos los resultados siguientes:",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_nueve_newton), // Recurso drawable
            contentDescription = "Home header background",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.graph_uno_newton), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Observe la duplicación de la exactitud en f (x) (y también en x) hasta que se encuentra la máxima precisión de app. La gráfica en computadora muestra tres iteraciones del método de Newton para este problema de ejemplo.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Usando el software encontramos que el polinomio tiene una sola raíz real, 2.17456.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}
