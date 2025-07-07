package com.example.mathsus_ia.ui.features

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun HomeHeader(
    method: String,
    description: String,

    ) {

    val homeHeaderBg = "https://i.imgur.com/vxPn35h.png"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = homeHeaderBg,
            contentDescription = "Home header background", // Provide a descriptive content descriptioncontentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                text = method,
                color = Color.Red,
                //fontFamily = OverPassFontFamily,
                //fontWeight = FontWeight.Medium,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                letterSpacing = (-1).sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Justify
            )


            Text(
                text = description,
                color = Color.Black,
                //fontFamily = OverPassFontFamily,
                //fontWeight = FontWeight.Light,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                letterSpacing = ((-0.2).sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Ajusta el ancho y agrega relleno
                textAlign = TextAlign.Justify // Alinea el texto de manera justificada
            )
        }
    }

}

