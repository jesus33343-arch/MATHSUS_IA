package com.example.mathsus_ia.ui.features.nav_menu_falsi

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.features.BottomNavBarFalsi
import kotlin.math.pow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.mathsus.ui.features.nav_menu_falsi.DestinosFalsi
import com.example.mathsus.ui.features.nav_menu_falsi.DrawerFalsi



@Composable
fun InformationFalsi(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosFalsi.Home,
        DestinosFalsi.CalculateFalsiScreen,
        DestinosFalsi.StepFalsiScreen,
        DestinosFalsi.ExcersiceFalsiScreen
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
                        InfoFalsi()
                    }
                }
            },
            bottomBar = { BottomNavBarFalsi(navController = navController) },
        )
    }
}

@Composable
fun InfoFalsi() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Método de Falsa Posición\n" ,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Justify
        )
        Text(
            text = "Aun cuando la Bisección es una técnica perfectamente válida para determinar raíces, su\n" +
                    "método de aproximación por “fuerza bruta” es relativamente ineficiente. La Falsa Posición\n" +
                    "es una alternativa basada en una visualización gráfica.",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Un inconveniente del método de Bisección es que al dividir el intervalo de (a,b) en mitades iguales,\n" +
                    "no se toman en consideración las magnitudes de f(a) y f(b). Por ejemplo, si f(a) está mucho más\n" +
                    "cercana a cero que f(b), es lógico que la raíz se encuentre más cerca de 'a' que de 'b'.\n" +
                    "Un método alternativo que aprovecha esta visualización gráfica consiste en unir f(a) y f(b) \n" +
                    "con una línea recta.La intersección de esta línea con el eje de las x representa una mejor \n" +
                    "aproximación de la raíz. El hecho de que se reemplace la curva por una línea recta da una\n" +
                    "“Falsa Posición” de la raíz; de aquí el nombre de método de la Falsa Posición, o en latín,\n" +
                    "regula falsi. También se le conoce como método de interpolacion lineal.\n",
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Usando triángulos semejantes, la intersección de la línea recta con el eje de las x se estima mediante\n" ,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.info_falsi_uno), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "en la cual se despeja 'c'",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.info_falsi_dos), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ésta es la fórmula de la Falsa Posición. El valor de 'c' se reemplazará, después, a cualquiera de los dos valores iniciales, 'a'\n" +
                    " o 'b', y da un valor de la función con el mismo signo de f(c). De esta manera, los valores 'a'\n" +
                    " y 'b' siempre encierran la verdadera raíz. El proceso se repite hasta que la aproximación a la raíz sea adecuada.\n" +
                    "El algoritmo es idéntico al de la Bisección. Además, se usa el mismo criterio de terminación.\n",
            textAlign = TextAlign.Justify
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Ajusta el tamaño de la gráfica
        ) {
            GraphPlot()
        }

        Text(
            text = "Desventajas del método de la Falsa Posición.\n",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Aunque el método de la Falsa Posición parecería ser siempre la mejor opción entre los\n" +
                    "métodos cerrados, hay casos donde funciona de manera deficiente. En efecto, como en\n" +
                    "el ejemplo siguiente, hay ciertos casos donde el método de Bisección ofrece mejores\n" +
                    "resultados.\n" ,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Planteamiento del problema.\n" ,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Con los métodos de Bisección y de Falsa Posición localice la raíz de\n" ,
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.info_falsi_tres), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "entre x = 0 y 1.3.\n" ,
            textAlign = TextAlign.Justify
        )

        Text(
            text = "Solución\n" ,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.info_falsi_cuatro), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = R.drawable.info_falsi_cinco), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "De esta manera, después de cinco iteraciones, el error verdadero se reduce a menos del\n" +
                    "2%. Con la Falsa Posición se obtienen resultados muy diferentes:\n" ,
            textAlign = TextAlign.Justify
        )

        Image(
            painter = painterResource(id = R.drawable.info_falsi_seis), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Después de cinco iteraciones, el error verdadero sólo se ha reducido al 59%. Además, observe que\n" +
                    "ea < et. Entonces, el error aproximado es engañoso. Se obtiene mayor claridad sobre estos \n" +
                    "resultados examinando  una gráfica de la función. En la curva viola la premisa sobre la  \n" +
                    "cual se basa la Falsa Posición; es decir, si f(a) se encuentra mucho más cerca de cero que \n" +
                    "f(b), la raíz se encuentra más cerca de 'a' que de 'b'. Sin embargo, debido a la forma de" +
                    " esta función ocurre lo contrario.\n" ,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "El ejemplo anterior ilustra que, por lo común, no es posible realizar generalizaciones\n" +
                    "con los métodos de obtención de raíces. Aunque un método como el de la Falsa Posición\n" +
                    "casi siempre es superior al de Bisección, hay algunos casos que violan esta conclusión\n" +
                    "general. Por lo tanto, además de usar la ecuación, los resultados se deben verificar\n" +
                    "sustituyendo la raíz aproximada en la ecuación original y determinar si el resultado se\n" +
                    "acerca a cero. Esta prueba se debe incorporar en todos los programas que localizan\n" +
                    "raíces.\n" ,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "El ejemplo ilustra también una importante desventaja del método de la Falsa Posición:\n" +
                    "su unilateralidad. Es decir, conforme se avanza en las iteraciones, uno de los puntos\n" +
                    "limitantes del intervalo tiende a permanecer fijo. Esto puede llevar a una mala convergencia," +
                    "especialmente en funciones con una curvatura importante. \n",
            textAlign = TextAlign.Justify
        )

    }

}

@Composable
fun GraphPlot() {
    val axisColor = MaterialTheme.colorScheme.onSurface
    val curveColor = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        val width = size.width
        val height = size.height

        // Definir puntos clave
        val xMin = width * 0.2f
        val xMax = width * 0.8f
        val yMin = height * 0.8f
        val yMax = height * 0.2f

        // Función f(x) invertida para que sea creciente
        fun f(x: Float): Float {
            val normX = (x - xMin) / (xMax - xMin) // Normalización 0-1
            return yMin - (yMin - yMax) * normX.pow(2) // Ajuste parabólico
        }

        val xL = xMin
        val xU = xMax
        val xR = (xL + xU) / 2

        val yL = f(xL)
        val yU = f(xU)
        val yR = f(xR)

        // Ajustar eje X para pasar por el punto medio
        val yAxisHeight = yR

        // Dibujar ejes coordenados
        drawLine(axisColor, Offset(xMin * 0.5f, height), Offset(xMin * 0.5f, 0f), strokeWidth = 5f)
        drawLine(axisColor, Offset(0f, yAxisHeight), Offset(width, yAxisHeight), strokeWidth = 5f)

        // Dibujar curva azul
        val path = Path().apply {
            moveTo(xL, yL)
            for (x in (xL.toInt()..xU.toInt() step 10)) {
                lineTo(x.toFloat(), f(x.toFloat()))
            }
        }
        drawPath(path, curveColor, style = Stroke(width = 4f))

        // Dibujar línea recta
        drawLine(axisColor, Offset(xL, yL), Offset(xU, yU), strokeWidth = 4f)

        // Sombreado entre línea recta y curva
        val shadedPath = Path().apply {
            moveTo(xL, yL)
            lineTo(xU, yU)
            lineTo(xU, f(xU))
            lineTo(xL, f(xL))
            close()
        }
        drawPath(shadedPath, axisColor.copy(alpha = 0.2f))

        // Dibujar puntos clave
        val pointColor = axisColor
        listOf(Offset(xL, yL), Offset(xU, yU), Offset(xR, yR)).forEach { point ->
            drawCircle(pointColor, radius = 8f, center = point)
        }
    }
}

