package com.example.mathsus_ia.ui.features
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.datasource.RawResourceDataSource
import androidx.navigation.NavHostController
import com.example.mathsus_ia.ui.methods.FAB
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun Info(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Sobre MATHSUS",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Information(navController = navController)
                }
            }
        },
        floatingActionButton = { FAB(navController = navController) }

    )
}

@Composable
fun Information(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Version ${io.github.jesusgurrute.mathsus_ia.BuildConfig.VERSION_NAME}",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            VideoPlayer()
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Descripción",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "MATHSUS es una herramienta matemática diseñada para estudiantes, ingenieros e investigadores que necesitan encontrar raíces de funciones no lineales en una variable. Esta aplicación implementa cuatro métodos numéricos populares - Bisección, Regla Falsa (Falsi), Newton-Raphson y Secante.",
            fontSize = 16.sp, textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Características principales:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "1. Resuelve ecuaciones utilizando los métodos de Bisección, Regla Falsa, Newton-Raphson o Secante\n" +
                    "2. Introduce funciones personalizadas con una interfaz fácil de usar.\n" +
                    "3. Visualiza el proceso de búsqueda de raíces con gráficos interactivos.\n" +
                    "4. Compara la eficiencia y precisión de los diferentes métodos.\n" +
                    "5. Desglose en una tabla las iteraciones paso a paso con fines educativos.\n",
            fontSize = 16.sp, textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ya sea que estés abordando ecuaciones polinómicas, funciones trascendentales u otras expresiones matemáticas complejas, MATHSUS proporciona una plataforma robusta e intuitiva para encontrar soluciones de manera rápida y precisa.",
            fontSize = 16.sp, textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Uso",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "1. Sintaxis básica",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)

            )
            Text(
                text = "• Usa paréntesis para agrupar operaciones: (2 + 3) * 4\n" +
                        "• Utiliza operadores estándar: +, -, *, /, ^(potencia)"
            )

            Text(
                text = "2. Funciones predefinidas",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = "• MATHSUS usa el parseador de mXparser donde incluye funciones como sin(), cos(), tan(), log(), etc.\n" +
                        "• Usa estas funciones con sus nombres en inglés: sin(x),  y no  sen(x)"
            )

            Text(
                text = "3. Constantes",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Puedes usar constantes predefinidas como pi, e, [phi] (número áureo)")

            Text(
                text = "4. Variables",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Define variables con letras. Por ejemplo, 'x' es común para funciones de una variable")

            Text(
                text = "5. Notación científica",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Usa 'E' para notación científica: 1.5E3 significa 1.5 * 10^3")

            Text(
                text = "6. Funciones personalizadas",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Puedes definir tus propias funciones: f(x) = 2*x + 3")

            Text(
                text = "7. Unidades de medida",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• MATHSUS soporta unidades, pero asegúrate de usarlas correctamente")

            Text(
                text = "8. Precisión",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Ten en cuenta la precisión de los cálculos, especialmente con operaciones complejas")

            Text(
                text = "9. Errores comunes",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = "• Evita espacios innecesarios\n" +
                        "• Asegúrate de cerrar todos los paréntesis\n" +
                        "• Usa el punto decimal, no la coma"
            )

            Text(
                text = "10. Documentación",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Consulta la documentación oficial de mXparser para funciones y sintaxis específicas")

            Text(
                text = "11. Pruebas",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(text = "• Siempre prueba tus funciones con valores conocidos para verificar su corrección")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ejemplo de una función bien formada:",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "f(x) = 2 * sin(x) + log(x, 10) - 3*x^2 + pi",
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Referencias adicionales",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Como lectura principal se uso el libro de Ward Cheney, David Kincaid - Metodos Numericos y Computacion-Cengage (2011),pero puede usar los siguientes textos como lecturas complementaria y de estudio, véase:\n" +
                    "\n[1] Barnsley [2006], Bus y Dekker [1975]\n" +
                    "[2] Dekker [1969]\n" +
                    "[3] Dennis y Schnabel [1983]\n" +
                    "[4] Epureanu y Greenside [1998]\n" +
                    "[5] Fauvel, Flood, Shortland y Wilson [1988]\n" +
                    "[6] Feder [1988], Ford [1995]\n" +
                    "[7] Householder [1970]\n" +
                    "[8] Kelley [1995]\n" +
                    "[9] Lozier y Olver [1994]\n" +
                    "[10] Nerinckx y Haegemans [1976]\n" +
                    "[11] Novak, Ritter y Woźniakowski [1995]\n" +
                    "[12] Ortega y Rheinboldt [1970]\n" +
                    "[13] Ostrowski [1966]\n" +
                    "[14] Rabinowitz [1970]\n" +
                    "[15] Traub [1964]\n" +
                    "[16] Westfall [1995] e Ypma [1995].",
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Desarrollador",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Diseñado y desarrollado por el estudiante del programa de Matemáticas Jesús Alirio Gurrute Campo para obtener su título de pregrado en la Universidad del Cauca.",
            modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))



        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "GitHub",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "https://github.com/JesusGurrute/MATHSUS",
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Contacto",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "¿Encontraste un error o tienes una sugerencia? Usa el botón de feedback: llega directo al equipo de desarrollo.",
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("feedback") }) {
            Text(text = "Enviar feedback para la próxima versión")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Apoya este proyecto",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "MATHSUS es gratis y sin ánimo de lucro. Si te sirvió y quieres ayudar a mantenerlo (servidor, IA, cuenta de desarrollador), cualquier aporte voluntario es bienvenido.",
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            textAlign = TextAlign.Justify
        )

        val donationContext = LocalContext.current
        Button(
            onClick = {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse("https://paypal.me/jesusgurrute")
                )
                donationContext.startActivity(intent)
            }
        ) {
            Text(text = "Apoyar con PayPal")
        }
    }
}

@Composable
fun VideoPlayer() {
    val context = LocalContext.current
    val videoUrl = "https://drive.google.com/uc?export=download&id=1Epp9sf2tpYmbQ00Fwb2GXwB8dktIFC9D"

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            StyledPlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f) // Ajusta esto según la relación de aspecto de tu video
    )
}