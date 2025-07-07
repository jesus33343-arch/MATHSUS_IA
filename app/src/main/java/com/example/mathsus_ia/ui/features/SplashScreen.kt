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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
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
import com.example.mathsus_ia.ui.theme.OverPassFontFamily


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
        val paddingValues = when {
            isSmallScreen -> 6.dp
            isMediumScreen -> 8.dp
            else -> 12.dp
        }
        val cornerRadius = when {
            isSmallScreen -> 8.dp
            isMediumScreen -> 12.dp
            else -> 16.dp
        }

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

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = paddingValues)
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    .padding(paddingValues)
            ) {
                Button(
                    onClick = {
                        navController.navigate("baking")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                    modifier = Modifier
                        .padding(
                            horizontal = when {
                                isSmallScreen -> 6.dp
                                isMediumScreen -> 7.dp
                                else -> 8.dp
                            },
                            vertical = when {
                                isSmallScreen -> 8.dp
                                isMediumScreen -> 10.dp
                                else -> 12.dp
                            }
                        )
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Back",
                        modifier = Modifier.size(
                            when {
                                isSmallScreen -> 18.dp
                                isMediumScreen -> 20.dp
                                else -> 24.dp
                            }
                        )
                    )
                    Spacer(modifier = Modifier.width(when {
                        isSmallScreen -> 2.dp
                        isMediumScreen -> 3.dp
                        else -> 4.dp
                    }))
                    Text(
                        text = "Preguntale a MATHSUS",
                        fontSize = when {
                            isSmallScreen -> 12.sp
                            isMediumScreen -> 13.sp
                            else -> 14.sp
                        }
                    )
                }

            }

            // Botones de navegación
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(horizontal = paddingValues)
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    .padding(paddingValues)
            ) {
                listOf(
                    listOf(
                        Triple("Bisección", "bisection", "Método\nBisec."),
                        Triple("Newton", "newton", "Newton\nRaphson")
                    ),
                    listOf(
                        Triple("Secante", "secante", "Método\nSec."),
                        Triple("Falsi", "falsi", "Regular\nFalsi")
                    )
                ).forEach { rowButtons ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowButtons.forEach { (name, route, shortText) ->
                            VerticalButton(
                                text = if (isSmallScreen) shortText else "Método\n$name",
                                fontSize = when {
                                    isSmallScreen -> 10.sp
                                    isMediumScreen -> 11.sp
                                    else -> 12.sp
                                },
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
                    .padding(paddingValues)
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = Color.White
                    )
                    .padding(paddingValues)
            ) {
                Text(
                    text = "MATHSUS",
                    color = Color.Red,
                    fontFamily = OverPassFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = when {
                        isSmallScreen -> 24.sp
                        isMediumScreen -> 28.sp
                        else -> 32.sp
                    },
                    letterSpacing = when {
                        isSmallScreen -> (-0.5).sp
                        isMediumScreen -> (-0.75).sp
                        else -> (-1).sp
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = when {
                            isSmallScreen -> 12.dp
                            isMediumScreen -> 14.dp
                            else -> 16.dp
                        }),
                    textAlign = TextAlign.Justify
                )

                Text(
                    text = "Esta calculadora resuelve el problema de la forma f(x) = 0, utilizando los métodos numéricos de la bisección, Newton - Raphson y el método de la secante.",
                    fontFamily = OverPassFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = when {
                        isSmallScreen -> 14.sp
                        isMediumScreen -> 16.sp
                        else -> 18.sp
                    },
                    lineHeight = when {
                        isSmallScreen -> 20.sp
                        isMediumScreen -> 22.sp
                        else -> 24.sp
                    },
                    letterSpacing = when {
                        isSmallScreen -> 0.sp
                        isMediumScreen -> (-0.05).sp
                        else -> (-0.1).sp
                    },
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = when {
                            isSmallScreen -> 12.dp
                            isMediumScreen -> 14.dp
                            else -> 16.dp
                        }),
                    textAlign = TextAlign.Justify
                )

                Button(
                    onClick = {
                        navController.navigate("info")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                    modifier = Modifier
                        .padding(
                            horizontal = when {
                                isSmallScreen -> 6.dp
                                isMediumScreen -> 7.dp
                                else -> 8.dp
                            },
                            vertical = when {
                                isSmallScreen -> 8.dp
                                isMediumScreen -> 10.dp
                                else -> 12.dp
                            }
                        )
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Back",
                        modifier = Modifier.size(
                            when {
                                isSmallScreen -> 18.dp
                                isMediumScreen -> 20.dp
                                else -> 24.dp
                            }
                        )
                    )
                    Spacer(modifier = Modifier.width(when {
                        isSmallScreen -> 2.dp
                        isMediumScreen -> 3.dp
                        else -> 4.dp
                    }))
                    Text(
                        text = "Conoce más",
                        fontSize = when {
                            isSmallScreen -> 12.sp
                            isMediumScreen -> 13.sp
                            else -> 14.sp
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun VerticalButton(
    text: String,
    fontSize: TextUnit,
    onClickAction: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Button(
        onClick = { onClickAction() },
        colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = text, fontSize = fontSize)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}