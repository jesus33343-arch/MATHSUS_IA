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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
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
fun LegacyInformationSecante(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosInfoSecante.Pantalla1,
        DestinosInfoSecante.Pantalla2,
        DestinosInfoSecante.Pantalla3,
        DestinosInfoSecante.Pantalla4
    )
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Drawer(menuItems = navigationItems, navController = navController)
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
                        LegacyInfoSecante()
                    }
                }
            },
            bottomBar = { BottomNavBarSecante(navController = navController) },
        )
    }
}

@Composable
fun LegacyInfoSecante() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Ahora consideraremos un procedimiento de propósito general que converge casi tan rápido como el método de Newton. Este método imita el de Newton pero evita el cálculo de derivadas. Recuerde que la iteración de Newton define a x_(n+1) en términos de x_n mediante la fórmula",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_cero_secante), // Recurso drawable
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "En el método de la Secante remplazamos f´(x_n) en la fórmula mediante una aproximación que se calcula fácilmente. Puesto que la derivada está definida por",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_uno_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "podemos decir que para h pequeña,",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.ec_dos_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = " En particular, si x = x_n y h = x_(n–1) – x_n, tenemos",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ec_tres_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cuando se usa esto en la primera ecuación, el resultado define el método de la Secante:",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ec_cuatro_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "El método de la Secante (parecido al de Newton) se puede usar para resolver también sistemas de ecuaciones.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "El nombre del método se toma del hecho de que el miembro derecho de la ecuación cuatro es la pendiente de una recta secante a la gráfica de f. Por supuesto, el miembro izquierdo es la pendiente de una recta tangente a la gráfica de f. (De manera similar, el método de Newton podría haberse llamado el “método de la tangente”.)",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.graph_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Pocas observaciones acerca de la ecuación cinco están en orden. Obviamente, x_(n+1) depende de dos elementos anteriores de la sucesiones. Así, para empezar se deben dar dos puntos (x₀ y x1). La ecuación cinco puede entonces generar x_2, x_3, . . .",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Al programar el método de la Secante podríamos calcular y probar la cantidad f(x_n) – f(x_(n–1). Si está cerca de cero, puede suceder un sobreflujo en la ecuación cinco. Por supuesto, si el método tiene éxito los puntos x_n tenderán a un cero de f, por lo que f(x_n) convergerá a cero. (Estamos suponiendo que f es continua.) También, f(x_(n–1) convergerá a cero y, con más razón, f(x_n) – f(x_(n–1) tenderá a cero. Si los términos f(x_n) y f(x_(n-1) tienen el mismo signo, los dígitos significativos adicionales se eliminan en la resta. Por ello, podríamos quizá detener la iteración cuando |f(x_n) – f(x_(n–1)|≤ δ |f(x_n)| con cierta tolerancia específica δ, tal como 1x10^(–6)",
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ejemplo", color = colorScheme.primary,
            textAlign = TextAlign.Justify,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Si el método de la Secante se usa en p(x) = x^5 + x^3 + 3 con x₀ = –1 y x1 = 1, ¿a qué es igual x_8?",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Solución", color = colorScheme.primary,
            textAlign = TextAlign.Justify,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "La salida de la aplicación usando el método de la Secante es la siguiente",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ejemplo_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Podemos usar MATHSUS para encontrar la única raíz real, –1.1053.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Resumen",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Justify
        )
        HorizontalDivider(thickness = 1.dp, color = colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "(1) El método de la Secante para determinar un cero r de una función f(x) se escribe como)",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ec_cinco_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "para n ≥ 1, que requiere dos valores iniciales x₀ y x1. Después del primer paso, sólo es necesaria una nueva evaluación de función por paso.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "(2) Después de n + 1 pasos del método de la Secante, el error de la iteración e_i = r – x_i obedece la ecuación",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ec_seis_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "que conduce a la aproximación",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ec_siete_secante),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Por tanto, el método de la Secante tiene un comportamiento de convergencia superlineal.",
            textAlign = TextAlign.Justify,
            color = colorScheme.onBackground,
            )
    }
}

