package com.example.mathsus_ia.ui.features

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val context = LocalContext.current
    var showHelpDialog by remember { mutableStateOf(false) }
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
            topBar = {
                TopBar(
                    title = "Método de Bisección",
                    scope = scope,
                    drawerState = drawerState,
                    actions = { BisectionTopBarActions(context, onHelpClick = { showHelpDialog = true }) }
                )
            },
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
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            BodyBisection()
                        }
                    }
                }
            },
            bottomBar = { BottomNavBarBisection(navController = navController) },
        )
    }

    if (showHelpDialog) {
        SymbolHelpDialog(onDismiss = { showHelpDialog = false })
    }
}

@Composable
private fun BisectionTopBarActions(
    context: android.content.Context,
    onHelpClick: () -> Unit
) {
    IconButton(onClick = onHelpClick) {
        Text(
            text = "?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
    IconButton(
        onClick = {
            Toast.makeText(
                context,
                "Próximamente: dibuja tu función y la IA la interpretará",
                Toast.LENGTH_LONG
            ).show()
        }
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Dibujar función (próximamente)",
            tint = Color.White
        )
    }
}

@Composable
private fun SymbolHelpDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Cómo escribo los símbolos?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    HelpCard(
                        title = "Función bien formada",
                        body = "f(x) = 2 * sin(x) + log(x, 10) - 3*x^2 + pi\nOperadores: +, -, *, /, ^ (potencia)"
                    )
                    HelpCard(
                        title = "Uso de paréntesis",
                        body = "(2 + 3) * 4\nCierra todos los paréntesis"
                    )
                    HelpCard(
                        title = "Funciones en inglés",
                        body = "sin(), cos(), tan(), log(), etc.\nUsa sin(x) y no sen(x)"
                    )
                    HelpCard(
                        title = "Constantes predefinidas",
                        body = "pi, e, [phi] (número áureo)\nUsa el punto decimal y no la coma"
                    )
                    HelpCard(
                        title = "Recuerda",
                        body = "x es la letra definida como variable\nPrueba con valores conocidos"
                    )
                }
            }
        }
    }
}

@Composable
private fun HelpCard(title: String, body: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
            icon = { Text("⌂", color = Color.White, fontSize = 22.sp) },
            label = { Text("Inicio", color = Color.White) },
            selected = false, // Cambia esto según la navegación actual
            onClick = { navController.navigate("splash") }
        )
        NavigationBarItem(
            icon = {
                Text(
                    "ƒ",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            },
            label = { Text("Método", color = Color.White) },
            selected = false, // Cambia esto según la navegación actual
            onClick = { navController.navigate("bisection") }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Paso a paso",
                    tint = Color.White
                )
            },
            label = { Text("Paso paso", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("pasoBisection") }
        )
        NavigationBarItem(
            icon = { Text("ⓘ", color = Color.White, fontSize = 20.sp) },
            label = { Text("Info", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("infoBisection") }
        )
        NavigationBarItem(
            icon = { Text("✎", color = Color.White, fontSize = 20.sp) },
            label = { Text("Práctica", color = Color.White) },
            selected = false,
            onClick = { navController.navigate("exerciseBisection") }
        )
    }
}
