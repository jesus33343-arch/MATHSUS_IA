package com.example.mathsus_ia.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mathsus.ui.features.nav_menu_secante.Drawer
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus.ui.features.nav_menu_secante.DestinosSecante
import com.example.mathsus_ia.ui.methods.secanteMethod.BodySecante
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun SecanteScreen(navController: NavHostController) {
    val splashUrl = R.drawable.mathsus_screen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosSecante.Pantalla1,
        DestinosSecante.Pantalla2,
        DestinosSecante.Pantalla3,
        DestinosSecante.Pantalla4
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
            topBar = { TopBar("Método de la secante", scope, drawerState) },
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
                            BodySecante()
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
            bottomBar = { BottomNavBarSecante(navController = navController) },
        )
    }
}


@Composable
fun BottomNavBarSecante(navController: NavController) {
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
            onClick = { navController.navigate("secante") }
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
            onClick = { navController.navigate("pasoSecante") }
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
            onClick = { navController.navigate("infoSecante") }
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
            onClick = { navController.navigate("exerciseSecante") }
        )
    }
}

@Composable
fun UsoSecante() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(8.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(8.dp)
    ) {
        Text(
            text = "Función bien formada",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.rojounicauca),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = "f(x) = 2 * sin(x) + log(x, 10) - 3*x^2 + pi\n" +
                    "Operadores: +, -, *, /, ^(potencia)",
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            color = colorScheme.onBackground,
        )
    }
}

@Composable
fun UsoSecante1() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp)
    ) {
        Text(
            text = "Uso paréntesis",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.rojounicauca),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "(2 + 3) * 4\n" +
                    "Cierra todos los paréntesis",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp, textAlign = TextAlign.Center,
            color = colorScheme.onBackground,
        )
    }
}

@Composable
fun UsoSecante2() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp)
    ) {
        Text(
            text = "Funciones en inglés",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.rojounicauca),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "sin(), cos(), tan(), log(), etc.\n" +
                    "Usa sin(x) y no  sen(x)",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp, textAlign = TextAlign.Center,
            color = colorScheme.onBackground,
        )
    }
}

@Composable
fun UsoSecante3() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp)
    ) {
        Text(
            text = "Constantes predefinidas ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.rojounicauca),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "pi, e, [phi] (número áureo)\n" +
                    "Usa el punto decimal y no la coma",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp, textAlign = TextAlign.Center,
            color = colorScheme.onBackground,
        )
    }
}

@Composable
fun UsoSecante4() {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(10.dp)
            .background(shape = RoundedCornerShape(16.dp), color = colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp)
    ) {
        Text(
            text = "Recuerda",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.rojounicauca),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "x es la letra definida como variable\n" +
                    "Prueba con valores conocidos",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 15.sp, textAlign = TextAlign.Center,
            color = colorScheme.onBackground,
        )
    }
}
