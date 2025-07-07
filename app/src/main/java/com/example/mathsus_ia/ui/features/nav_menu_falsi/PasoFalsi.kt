package com.example.mathsus.ui.features.nav_menu_falsi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathsus.ui.features.nav_menu_secante.TopBar
import com.example.mathsus.ui.methods.falsiMethod.PasoBodyFalsi
import io.github.jesusgurrute.mathsus_ia.R
import com.example.mathsus_ia.ui.features.BottomNavBarFalsi

@Composable
fun PasoFalsi(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        DestinosFalsi.Home,
        DestinosFalsi.CalculateFalsiScreen,
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
            topBar = { TopBar("Regular falsi paso a paso", scope, drawerState) },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    PasoBodyFalsi()                 }
            },
            bottomBar = { BottomNavBarFalsi(navController = navController) }
        )
    }
}

@Composable
fun DrawerFalsi(menuItems: List<DestinosFalsi>, navController: NavHostController) {
    Column {
        Image(
            painterResource(id = R.drawable.menu_lateral),
            contentDescription = "Menú de opciones",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        )
        menuItems.forEach { item ->
            DrawerItemFalsi(item = item, navController = navController)
        }
    }
}

@Composable
fun DrawerItemFalsi(item: DestinosFalsi, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                navController.navigate(item.ruta) {
                    launchSingleTop = true
                }
            }
            .padding(8.dp)
    ) {
        Image(
            painterResource(id = item.icon),
            contentDescription = item.title,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


