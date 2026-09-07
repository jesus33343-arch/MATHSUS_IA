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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import com.example.mathsus.ui.features.nav_menu_falsi.DestinosFalsi
import com.example.mathsus.ui.features.nav_menu_falsi.DrawerFalsi
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus.ui.methods.falsiMethod.BodyFalsi
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun FalsiScreen(navController: NavHostController) {
    val splashUrl = R.drawable.mathsus_screen
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosFalsi.Home,
        DestinosFalsi.StepFalsiScreen,
        DestinosFalsi.InfoFalsiScreen,
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
            topBar = { TopBar("Método de Regular Falsi", scope, drawerState) },
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
                            BodyFalsi()
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
            bottomBar = { BottomNavBarFalsi(navController = navController) },
        )
    }
}

@Composable
fun BottomNavBarFalsi(navController: NavController) {
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
            onClick = { navController.navigate("falsi") }
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
            onClick = { navController.navigate("pasoFalsi") }
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
            onClick = { navController.navigate("infoFalsi") }
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
            onClick = { navController.navigate("exerciseFalsi") }
        )
    }
}
