package com.example.mathsus_ia.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.github.jesusgurrute.mathsus_ia.R

private data class GlobalMenuItem(val title: String, val route: String, val icon: ImageVector? = null, val iconRes: Int? = null, val symbol: String? = null)

@Composable
fun GlobalDrawerContent(navController: NavHostController) {
    val items = listOf(
        GlobalMenuItem("Inicio", "splash", Icons.Default.Home),
        GlobalMenuItem("Bisección", "bisection", symbol = "ƒ"),
        GlobalMenuItem("Regular Falsi", "falsi", symbol = "ƒ"),
        GlobalMenuItem("Newton-Raphson", "newton", symbol = "ƒ"),
        GlobalMenuItem("Secante", "secante", symbol = "ƒ"),
        GlobalMenuItem("Pregúntale a MATHSUS", "baking", iconRes = R.drawable.ic_robot),
        GlobalMenuItem("Conoce más", "info", Icons.Default.Info)
    )
    Column {
        Image(painterResource(R.drawable.menu_lateral), "Menú principal", Modifier.fillMaxWidth().height(160.dp), contentScale = ContentScale.FillWidth)
        Spacer(Modifier.height(12.dp))
        items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 6.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(12.dp)).clickable { navController.navigate(item.route) { launchSingleTop = true } }
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                if (item.icon != null) {
                    Icon(item.icon, item.title, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                } else if (item.iconRes != null) {
                    Icon(painterResource(item.iconRes), item.title, Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                } else {
                    Text(item.symbol.orEmpty(), modifier = Modifier.size(32.dp), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.width(12.dp))
                Text(item.title, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
