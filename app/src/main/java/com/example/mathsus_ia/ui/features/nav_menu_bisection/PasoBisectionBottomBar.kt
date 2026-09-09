package com.example.mathsus.ui.features.nav_menu_bisection

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun BottomNavBarBisectionUpdated(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = colorResource(id = R.color.azulunicauca),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Inicio", tint = Color.White) },
            label = { Text("Inicio", color = Color.White) },
            selected = currentRoute == "splash",
            onClick = { navController.navigate("splash") }
        )
        NavigationBarItem(
            icon = { Text("ƒ", color = Color.White, fontSize = 22.sp, fontStyle = FontStyle.Italic) },
            label = { Text("Método", color = Color.White) },
            selected = currentRoute == "bisection",
            onClick = { navController.navigate("bisection") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, "Paso a paso", tint = Color.White) },
            label = { Text("Paso paso", color = Color.White) },
            selected = currentRoute == "pasoBisection",
            onClick = { navController.navigate("pasoBisection") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, "Info", tint = Color.White) },
            label = { Text("Info", color = Color.White) },
            selected = currentRoute == "infoBisection",
            onClick = { navController.navigate("infoBisection") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Edit, "Práctica", tint = Color.White) },
            label = { Text("Práctica", color = Color.White) },
            selected = currentRoute == "exerciseBisection",
            onClick = { navController.navigate("exerciseBisection") }
        )
    }
}
