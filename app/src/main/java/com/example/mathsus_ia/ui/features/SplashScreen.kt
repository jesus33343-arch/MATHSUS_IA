package com.example.mathsus_ia.ui.features

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.github.jesusgurrute.mathsus_ia.R

@Composable
fun SplashScreen(navController: NavController) {
    BoxWithConstraints {
        val colorScheme = MaterialTheme.colorScheme
        val splashUrl = R.drawable.mathsus_screen

        // Identificación de tamaños de pantalla basada en el ancho máximo
        val isSmallScreen = maxWidth < 360.dp
        val isMediumScreen = maxWidth in 360.dp..410.dp

        // Carga de imagen con AsyncImagePainter
        val painter = rememberAsyncImagePainter(
            model = splashUrl,
            onError = {
                Log.e("SplashScreen", "Error loading image: ${it.result.throwable}")
            }
        )

        // Definición de tamaños responsivos
        val cardPadding = when {
            isSmallScreen -> 12.dp
            isMediumScreen -> 14.dp
            else -> 16.dp
        }
        val cardSpacing = when {
            isSmallScreen -> 12.dp
            isMediumScreen -> 14.dp
            else -> 16.dp
        }
        val cornerRadius = 20.dp

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Indicador de carga
            if (painter.state is AsyncImagePainter.State.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }

            // Imagen de fondo
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = cardSpacing, vertical = 12.dp)
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = colorScheme.surface.copy(alpha = 0.95f)
                    )
                    .padding(cardPadding)
            ) {
                Button(
                    onClick = { navController.navigate("baking") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Pregúntale a MATHSUS",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // Botones de navegación a los 4 métodos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(cardSpacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .offset(y = 48.dp)
                    .padding(horizontal = cardSpacing)
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = colorScheme.surface.copy(alpha = 0.95f)
                    )
                    .padding(cardPadding)
            ) {
                listOf(
                    listOf(
                        Triple("Bisección", "bisection", "Bisección"),
                        Triple("Newton", "newton", "Newton\nRaphson")
                    ),
                    listOf(
                        Triple("Secante", "secante", "Secante"),
                        Triple("Falsi", "falsi", "Regular\nFalsi")
                    )
                ).forEach { rowButtons ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(cardSpacing),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowButtons.forEach { (_, route, shortText) ->
                            VerticalButton(
                                text = shortText,
                                fontSize = when {
                                    isSmallScreen -> 12.sp
                                    isMediumScreen -> 13.sp
                                    else -> 14.sp
                                },
                                modifier = Modifier.weight(1f),
                                onClickAction = { navController.navigate(route) }
                            )
                        }
                    }
                }
            }

            // Información de la aplicación
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = cardSpacing, vertical = 12.dp)
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = colorScheme.surface
                    )
                    .padding(cardPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "MATHSUS",
                    color = colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Esta calculadora resuelve el problema de la forma",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "f(x) = 0",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "utilizando el método de Bisección, método de Regular Falsi, método de Newton - Raphson y el método de la Secante.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Button(
                    onClick = { navController.navigate("info") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Conoce más", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun VerticalButton(
    text: String,
    fontSize: TextUnit,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Button(
        onClick = { onClickAction() },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(14.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
        modifier = modifier.height(64.dp)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
