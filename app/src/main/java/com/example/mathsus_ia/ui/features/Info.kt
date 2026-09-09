package com.example.mathsus_ia.ui.features
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.example.mathsus_ia.data.IdeaRequestSubmission
import com.example.mathsus_ia.data.submitIdeaRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Info(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Sobre MATHSUS",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                SupportBanner()

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Information(navController = navController)
                }
            }
        }
    )
}

private fun openInBrowserOrFallback(context: android.content.Context, url: String) {
    val uri = android.net.Uri.parse(url)
    // Se fuerza Chrome porque si hay una app nativa instalada que reclama ese
    // dominio (App Links, como la app de PayPal), Android se la entrega directo
    // a esa app en vez de mostrar la página web con todas las opciones de pago.
    val chromeIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri).apply {
        setPackage("com.android.chrome")
    }
    try {
        context.startActivity(chromeIntent)
    } catch (e: android.content.ActivityNotFoundException) {
        val fallbackIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
        context.startActivity(fallbackIntent)
    }
}

@Composable
private fun SupportBanner() {
    val donationContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Apoya este proyecto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "MATHSUS es gratis. Tu aporte ayuda a mantenerlo.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    openInBrowserOrFallback(donationContext, "https://paypal.me/jesusgurrute")
                }
            ) {
                Text(text = "PayPal")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    openInBrowserOrFallback(donationContext, "https://checkout.wompi.co/l/VPOS_nrvX1T")
                }
            ) {
                Text(text = "PSE / Nequi")
            }
        }
    }
}

@Composable
fun Information(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        ) {
            VideoPlayer()
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle("Descripción")

        Spacer(modifier = Modifier.height(8.dp))

        BodyText(
            "MATHSUS es una herramienta matemática diseñada para estudiantes, ingenieros e investigadores que necesitan encontrar raíces de funciones no lineales en una variable. Esta aplicación implementa cuatro métodos numéricos populares: Bisección, Regular Falsi, Newton-Raphson y Secante."
        )

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle("Características principales")

        Spacer(modifier = Modifier.height(8.dp))

        BodyText(
            "1. Resuelve ecuaciones utilizando los métodos de Bisección, Regular Falsi, Newton-Raphson o Secante.\n" +
                    "2. Introduce funciones personalizadas con una interfaz fácil de usar.\n" +
                    "3. Visualiza el proceso de búsqueda de raíces con gráficos interactivos.\n" +
                    "4. Compara la eficiencia y precisión de los diferentes métodos.\n" +
                    "5. Desglosa en una tabla las iteraciones paso a paso con fines educativos."
        )

        Spacer(modifier = Modifier.height(12.dp))

        BodyText(
            "Ya sea que estés abordando ecuaciones polinómicas, funciones trascendentales u otras expresiones matemáticas complejas, MATHSUS proporciona una plataforma robusta e intuitiva para encontrar soluciones de manera rápida y precisa."
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Uso")

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            UsageItem(
                "1. Sintaxis básica",
                "• Usa paréntesis para agrupar operaciones: (2 + 3) * 4\n" +
                        "• Utiliza operadores estándar: +, -, *, /, ^ (potencia)"
            )
            UsageItem(
                "2. Funciones predefinidas",
                "• MATHSUS usa el parseador de mXparser, que incluye funciones como sin(), cos(), tan(), log(), etc.\n" +
                        "• Usa estas funciones con sus nombres en inglés: sin(x), y no sen(x)"
            )
            UsageItem(
                "3. Constantes",
                "• Puedes usar constantes predefinidas como pi, e, [phi] (número áureo)"
            )
            UsageItem(
                "4. Variables",
                "• Define variables con letras. Por ejemplo, 'x' es común para funciones de una variable"
            )
            UsageItem(
                "5. Notación científica",
                "• Usa 'E' para notación científica: 1.5E3 significa 1.5 * 10^3"
            )
            UsageItem(
                "6. Funciones personalizadas",
                "• Puedes definir tus propias funciones: f(x) = 2*x + 3"
            )
            UsageItem(
                "7. Unidades de medida",
                "• MATHSUS soporta unidades, pero asegúrate de usarlas correctamente"
            )
            UsageItem(
                "8. Precisión",
                "• Ten en cuenta la precisión de los cálculos, especialmente con operaciones complejas"
            )
            UsageItem(
                "9. Errores comunes",
                "• Evita espacios innecesarios\n" +
                        "• Asegúrate de cerrar todos los paréntesis\n" +
                        "• Usa el punto decimal, no la coma"
            )
            UsageItem(
                "10. Documentación",
                "• Consulta la documentación oficial de mXparser para funciones y sintaxis específicas"
            )
            UsageItem(
                "11. Pruebas",
                "• Siempre prueba tus funciones con valores conocidos para verificar su corrección"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ejemplo de una función bien formada:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "f(x) = 2 * sin(x) + log(x, 10) - 3*x^2 + pi",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Referencias adicionales")

        Spacer(modifier = Modifier.height(10.dp))

        BodyText(
            "Como lectura principal se usó el libro de Ward Cheney y David Kincaid, Métodos Numéricos y Computación, Cengage (2011). También puede usar los siguientes textos como lecturas complementarias de estudio:\n" +
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
                    "[16] Westfall [1995] e Ypma [1995]."
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Desarrollador")

        Spacer(modifier = Modifier.height(8.dp))

        BodyText(
            "Diseñado y desarrollado por el estudiante del programa de Matemáticas Jesús Alirio Gurrute Campo para obtener su título de pregrado en la Universidad del Cauca."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Versión ${io.github.jesusgurrute.mathsus_ia.BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle("GitHub")

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "https://github.com/jesus33343-arch/MATHSUS_IA",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Contacto")

        Spacer(modifier = Modifier.height(8.dp))

        BodyText(
            "¿Encontraste un error o tienes una sugerencia? Usa el botón de feedback: llega directo al equipo de desarrollo."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("feedback") }) {
            Text(text = "Enviar feedback para la próxima versión")
        }

        Spacer(modifier = Modifier.height(24.dp))
        IdeaRequestSection()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun IdeaRequestSection() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var audience by rememberSaveable { mutableStateOf("estudiante") }
    var institution by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var idea by rememberSaveable { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var sent by remember { mutableStateOf(false) }

    SectionTitle("¿Tienes una idea?")
    Spacer(modifier = Modifier.height(8.dp))
    BodyText("¿Te gustaría llevar más matemáticas en el bolsillo o crear una aplicación como MATHSUS? Cuéntanos tu idea con el mayor detalle posible. El equipo la estudiará y podremos contactarte para explorarla o cotizar su desarrollo.")
    Spacer(modifier = Modifier.height(12.dp))
    Text("¿Quién eres?", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        listOf("estudiante" to "Estudiante", "docente" to "Docente", "otro" to "Otro").forEach { (value, label) ->
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = audience == value, onClick = { audience = value })
                Text(label, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
    OutlinedTextField(value = institution, onValueChange = { if (it.length <= 200) institution = it }, label = { Text("Universidad o institución (opcional)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = email, onValueChange = { if (it.length <= 320) email = it }, label = { Text("Correo para contactarte (opcional)") }, singleLine = true, modifier = Modifier.fillMaxWidth(), keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email))
    OutlinedTextField(value = idea, onValueChange = { if (it.length <= 4000) idea = it }, label = { Text("Describe tu idea") }, minLines = 5, modifier = Modifier.fillMaxWidth())
    Spacer(modifier = Modifier.height(8.dp))
    Button(enabled = !sending && idea.trim().length >= 20, onClick = {
        sending = true
        scope.launch {
            try {
                submitIdeaRequest(IdeaRequestSubmission(audience, institution.trim().ifBlank { null }, email.trim().ifBlank { null }, idea.trim(), io.github.jesusgurrute.mathsus_ia.BuildConfig.VERSION_NAME))
                sent = true
                institution = ""
                email = ""
                idea = ""
                Toast.makeText(context, "¡Gracias! Recibimos tu idea.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "No se pudo enviar. Revisa tu conexión.", Toast.LENGTH_LONG).show()
            } finally { sending = false }
        }
    }, modifier = Modifier.fillMaxWidth()) {
        Text(if (sending) "Enviando…" else if (sent) "Enviar otra idea" else "Enviar mi idea")
    }
    Text("El correo es opcional y solo se usará si necesitamos contactarte sobre tu propuesta.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun BodyText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Justify
    )
}

@Composable
private fun UsageItem(title: String, body: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
    Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun VideoPlayer() {
    val context = LocalContext.current
    val videoUrl = "https://drive.usercontent.google.com/download?id=1Epp9sf2tpYmbQ00Fwb2GXwB8dktIFC9D&export=download&confirm=t"

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
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
                useController = true
                controllerAutoShow = false
                setShowBuffering(StyledPlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                setKeepContentOnPlayerReset(true)
                setResizeMode(com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f) // Ajusta esto según la relación de aspecto de tu video
    )
}
