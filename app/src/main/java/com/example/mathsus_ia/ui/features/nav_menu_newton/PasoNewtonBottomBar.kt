package com.example.mathsus_ia.ui.features.nav_menu_newton

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun BottomNavBarNewtonUpdated(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(modifier = Modifier.navigationBarsPadding(), containerColor = colorResource(R.color.azulunicauca), tonalElevation = 8.dp) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, "Inicio", tint = Color.White) }, label = { Text("Inicio", color = Color.White) }, selected = currentRoute == "splash", onClick = { navController.navigate("splash") })
        NavigationBarItem(icon = { Text("ƒ", color = Color.White, fontSize = 22.sp, fontStyle = FontStyle.Italic) }, label = { Text("Método", color = Color.White) }, selected = currentRoute == "newton", onClick = { navController.navigate("newton") })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, "Paso a paso", tint = Color.White) }, label = { Text("Paso paso", color = Color.White) }, selected = currentRoute == "pasoNewton", onClick = { navController.navigate("pasoNewton") })
        NavigationBarItem(icon = { Icon(Icons.Default.Info, "Info", tint = Color.White) }, label = { Text("Info", color = Color.White) }, selected = currentRoute == "infoNewton", onClick = { navController.navigate("infoNewton") })
        NavigationBarItem(icon = { Icon(Icons.Default.Edit, "Práctica", tint = Color.White) }, label = { Text("Práctica", color = Color.White) }, selected = currentRoute == "exerciseNewton", onClick = { navController.navigate("exerciseNewton") })
    }
}
