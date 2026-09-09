package com.example.mathsus_ia.ui.features.nav_menu_bisection

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
import androidx.compose.material3.MaterialTheme
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
import com.example.mathsus.ui.features.nav_menu_bisection.BottomNavBarBisection
import com.example.mathsus.ui.features.nav_menu_bisection.DestinosBisection
import com.example.mathsus.ui.features.nav_menu_bisection.DrawerBisection
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus.ui.features.nav_menu_secante.TopBar

@Composable
fun LegacyInformationBisection(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosBisection.Home,
        DestinosBisection.CalculateBisectionScreen,
        DestinosBisection.StepBisectionScreen,
        DestinosBisection.ExcersiceBisectionScreen
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerBisection(menuItems = navigationItems, navController = navController)
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
                        LegacyInfoBisection()
                    }
                }
            },
            bottomBar = { BottomNavBarBisection(navController = navController) },
        )
    }

}

@Composable
fun LegacyInfoBisection() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sea f una función con valores reales o complejos de una variable real o compleja. Un número r, real o complejo, para el que f(r) = 0 se llama una raíz de la ecuación o un cero de f. Por ejemplo, la función",
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_cero_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "tiene 1/2 y 2/3 como ceros, como se puede comprobar sustituyendo directamente o escribiendo f en su forma factorizada:",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_uno_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Otro ejemplo, la función",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_dos_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "no sólo tiene el cero obvio x = 0, sino también todos los múltiplos enteros de π/5 y de π/2, lo que descubrimos al aplicar la identidad trigonométrica",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_tres_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Por tanto, encontramos",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_cuatro_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Por qué es importante localizar raíces? Con frecuencia, la solución de un problema científico es un número del que tenemos poca información y sólo sabemos que satisface cierta ecuación. Puesto que toda ecuación puede escribirse de tal manera que la función se encuentre en un miembro y cero en el otro, el número que se quiere determinar debe ser un cero de la función. Así, si tenemos un conjunto de métodos para localizar ceros de funciones podremos resolver dichos problemas.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ejemplificamos esta aseveración al usar un problema específico de ingeniería cuya solución es la raíz de una ecuación. En un cierto circuito eléctrico, el voltaje V y la corriente I están relacionados con dos ecuaciones de la forma",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_cinco_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "en las que a, b, c y d son constantes. Para nuestro objetivo, se supone que se conocen estos cuatro números. Cuando se combinan estas ecuaciones al eliminarse I entre ellas, el resultado es una ecuación simple:",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_seis_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "En un caso concreto, se puede reducir a",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_siete_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "y se requiere su solución. (Despejando se encuentra que en este caso V = 0.299.)\n" +
                    "\nEn algunos problemas en los que se busca la raíz de una ecuación podemos realizar el cálculo necesario con una calculadora manual. Pero ¿cómo podemos localizar ceros de funciones tan complicadas como éstas?",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_ocho_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Se necesita un método numérico general que no dependa de las propiedades especiales de nuestras funciones. Por supuesto, la continuidad y la derivabilidad son propiedades especiales, pero son atributos comunes de funciones que normalmente se encuentran. La clase de propiedad especial que quizá no podemos aprovechar fácilmente en códigos de propósito general está tipificado por la identidad trigonométrica mencionada en párrafos atrás.\n" +
                    "\nCientos de métodos están disponibles para localizar ceros de funciones y se han seleccionado tres de los más útiles para estudiarlos aquí: el método de Bisección, el método de Newton y el método de la Secante.\n" +
                    "\nSea f una función que tiene valores de signos opuestos en los dos extremos de un intervalo. Suponga también que f es continua en ese intervalo. Para fijar la notación, sea a < b y f (a) f (b) < 0. Por ello f tiene una raíz en el intervalo (a, b). En otras palabras, debe existir un número r que satisface las dos condiciones a < r < b y f (r) = 0. ¿Cómo se llegó a esta conclusión? Se debe recordar el teorema del valor intermedio.* Si x recorre un intervalo [a, b], entonces los valores de f (x) llenan por completo el intervalo entre f (a) y f (b). No se pueden omitir los valores intermedios. Por tanto, una función específica f debe tomar el valor cero en alguna parte del intervalo (a, b), ya que f (a) y f (b) son de signos opuestos.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ejemplos",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ahora queremos mostrar cómo se pueden usar el seudocódigo de la Bisección. Suponga que tenemos dos funciones y para cada una buscamos un cero en un intervalo dado:",

            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_nueve_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Primero, escribimos dos funciones de procedimiento para calcular f (x) y g (x). Después introducimos los intervalos iniciales y el número de pasos que se darán en el programa principal. Puesto que se trata de un ejemplo sencillo, está información se podría asignar directamente en el programa principal o con expresiones en los subprogramas más que para que la lea el programa. También, según el lenguaje de computadora que se esté usando, se necesita un enunciado o interface externos para decirle al compilador que el parámetro f en el procedimiento de la Bisección no es una variable ordinaria con valores numéricos sino el nombre de una función de procedimiento definida externamente del programa principal. En este ejemplo, habría dos de estas funciones de procedimiento y dos llamadas al procedimiento de la Bisección\n" +
                    "\nLos resultados de computadora para los pasos iterativos del método de Bisección para f(x):",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_diez_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize(),
            //.height(100.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "También, los resultados para g(x) son los siguientes:",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_once_biseccion), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Para encontrar que las raíces deseadas de f y g son 0.34729 63553 y 1.23618 3928, respectivamente. Puesto que f es un polinomio, podemos usar una rutina para determinar aproximaciones numéricas a todos los ceros (o raíces) de una función polinomio. Sin embargo, cuando se trata de funciones polinomiales más complicadas, existe generalmente un procedimiento no sistemático para determinar todos los ceros. En este caso, se puede usar una rutina para localizar ceros (uno a la vez), pero tenemos que especificar un punto en el que ha de empezar la búsqueda, y puntos diferentes de inicio pueden dar como resultado el mismo o diferentes ceros. Puede ser particularmente problemático encontrar todas las raíces de una función cuyo comportamiento no se conoce.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

    }

}
