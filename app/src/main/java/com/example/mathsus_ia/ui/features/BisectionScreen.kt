package com.example.mathsus_ia.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mathsus.ui.features.nav_menu_bisection.DestinosBisection
import com.example.mathsus.ui.features.nav_menu_bisection.DrawerBisection
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus.ui.methods.bisectionMethod.BodyBisection
import io.github.jesusgurrute.mathsus_ia.R


@Composable
fun BisectionScreen(navController: NavHostController) {
    val splashUrl = R.drawable.mathsus_screen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosBisection.Home,
        DestinosBisection.StepBisectionScreen,
        DestinosBisection.InfoBisectionScreen,
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
            topBar = { TopBar("Método de Bisección", scope, drawerState) },
            content = { padding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = splashUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState()) // Agrega el scroll vertical aquí
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 200.dp, max = 450.dp)
                        ) {
                            BodyBisection()
                        }
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            Box(modifier = Modifier.width(330.dp)) {
                                UsoSecante()
                            }
                            Box(modifier = Modifier.width(330.dp)) {
                                UsoSecante1()
                            }
                            Box(modifier = Modifier.width(330.dp)) {
                                UsoSecante2()
                            }
                            Box(modifier = Modifier.width(330.dp)) {
                                UsoSecante3()
                            }
                            Box(modifier = Modifier.width(330.dp)) {
                                UsoSecante4()
                            }
                        }
                    }
                }
            },
            bottomBar = { BottomNavBarBisection(navController = navController) },
        )
    }
}
@Composable
fun BottomNavBarBisection(navController: NavController) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(), // Añadir padding para evitar superposición con la barra de navegación
        containerColor = colorResource(id = R.color.azulunicauca),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White) },
            label = { Text("Inicio", color = Color.White) },
            selected = false, // Cambia esto según la navegación actual
            onClick = { navController.navigate("splash") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Método",
                    tint = Color.White
                )
            },
            label = { Text("Método", color = Color.White) },
            selected = false, // Cambia esto según la navegación actual
            onClick = { navController.navigate("bisection") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Paso a paso", tint = Color.White
                )
            },
            label = { Text("Paso paso", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("pasoBisection") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Información",
                    tint = Color.White
                )
            },
            label = { Text("Info", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("infoBisection") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Ejercicios",
                    tint = Color.White
                )
            },
            label = { Text("Práctica", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("exerciseBisection") }
        )
    }
}

/*


        ---------------------------------------------------------------
Column {
        HomeHeader(
            "Método de la bisección",
            "Este método requiere un intervalo [a,b] donde la función sea continua y exista un cambio de signo.\n" +
                    "Las expresiones que puede usar son: sin(x), cos(x), tan(x), cot(x), sec(x), csc(x), log(x), ln(x), x^,e, pi, sqrt(x) y composición de estas teniendo en cuenta el uso correcto de los paréntesis con puntuacion en decimales en vez de comas."
        )
        Button(
            onClick = {
                navController.navigate("splash")
            }
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Regreso a la pantalla principal")
        }

        BodyBisection()
    }
 */