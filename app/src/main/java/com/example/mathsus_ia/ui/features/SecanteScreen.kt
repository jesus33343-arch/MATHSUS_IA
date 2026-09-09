package com.example.mathsus_ia.ui.features

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val context = LocalContext.current
    var showHelpDialog by remember { mutableStateOf(false) }
    var showDrawDialog by remember { mutableStateOf(false) }
    var drawnExpression by remember { mutableStateOf("") }
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
            topBar = {
                TopBar(
                    title = "Método de la Secante",
                    scope = scope,
                    drawerState = drawerState,
                    actions = { SecanteTopBarActionsV2(onHelpClick = { showHelpDialog = true }, onDrawClick = { showDrawDialog = true }) }
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
                            .verticalScroll(rememberScrollState()) // Agrega el scroll vertical aquí
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            BodySecante(initialFunction = drawnExpression)
                        }
                    }
                }
            },
            bottomBar = { BottomNavBarSecante(navController = navController) },
        )
    }

    if (showHelpDialog) {
        SymbolHelpDialogSecante(onDismiss = { showHelpDialog = false })
    }
    if (showDrawDialog) {
        DrawFunctionDialog(method = "Secante", onDismiss = { showDrawDialog = false }) { expression ->
            drawnExpression = expression
            Toast.makeText(context, "Funcion interpretada y copiada: $expression", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun SecanteTopBarActionsV2(onHelpClick: () -> Unit, onDrawClick: () -> Unit) {
    IconButton(onClick = onHelpClick) { Text("?", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) }
    IconButton(onClick = onDrawClick) {
        Icon(imageVector = Icons.Default.Edit, contentDescription = "Dibujar funcion e interpretar con IA", tint = Color.White)
    }
}

@Composable
private fun SecanteTopBarActions(
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
private fun SymbolHelpDialogSecante(onDismiss: () -> Unit) {
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
                    HelpCardSecante(
                        title = "Función bien formada",
                        body = "f(x) = 2 * sin(x) + log(x, 10) - 3*x^2 + pi\nOperadores: +, -, *, /, ^ (potencia)"
                    )
                    HelpCardSecante(
                        title = "Uso de paréntesis",
                        body = "(2 + 3) * 4\nCierra todos los paréntesis"
                    )
                    HelpCardSecante(
                        title = "Funciones en inglés",
                        body = "sin(), cos(), tan(), log(), etc.\nUsa sin(x) y no sen(x)"
                    )
                    HelpCardSecante(
                        title = "Constantes predefinidas",
                        body = "pi, e, [phi] (número áureo)\nUsa el punto decimal y no la coma"
                    )
                    HelpCardSecante(
                        title = "Recuerda",
                        body = "x es la letra definida como variable\nPrueba con valores conocidos"
                    )
                }
            }
        }
    }
}

@Composable
private fun HelpCardSecante(title: String, body: String) {
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
fun BottomNavBarSecante(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(), // Añadir padding para evitar superposición con la barra de navegación
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
            icon = {
                Text("ƒ", color = Color.White, fontSize = 22.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            },
            label = { Text("Método", color = Color.White) },
            selected = currentRoute == "secante",
            onClick = { navController.navigate("secante") }
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
            selected = currentRoute == "pasoSecante",
            onClick = { navController.navigate("pasoSecante") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, "Info", tint = Color.White) },
            label = { Text("Info", color = Color.White) },
            selected = currentRoute == "infoSecante",
            onClick = { navController.navigate("infoSecante") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Edit, "Práctica", tint = Color.White) },
            label = { Text("Práctica", color = Color.White) },
            selected = currentRoute == "exerciseSecante",
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
            color = MaterialTheme.colorScheme.primary,
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
            color = MaterialTheme.colorScheme.primary,
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
            color = MaterialTheme.colorScheme.primary,
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
            color = MaterialTheme.colorScheme.primary,
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
            color = MaterialTheme.colorScheme.primary,
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
