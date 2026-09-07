package com.example.mathsus_ia.ui.methods

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun FAB(navController: NavHostController) {
    FloatingActionButton(
        onClick = {
            navController.navigate("splash")
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Inicio"
        )
    }
}
