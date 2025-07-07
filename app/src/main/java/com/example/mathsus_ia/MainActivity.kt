package com.example.mathsus_ia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mathsus.ui.features.nav_menu_bisection.ExerciseBisection
import com.example.mathsus_ia.ui.features.nav_menu_bisection.InformationBisection
import com.example.mathsus.ui.features.nav_menu_bisection.PasoBisection
import com.example.mathsus.ui.features.nav_menu_falsi.ExerciseFalsi
import com.example.mathsus_ia.ui.features.nav_menu_falsi.InformationFalsi
import com.example.mathsus.ui.features.nav_menu_falsi.PasoFalsi
import com.example.mathsus.ui.features.nav_menu_secante.ExerciseSecante
import com.example.mathsus.ui.features.nav_menu_secante.InformationSecante
import com.example.mathsus.ui.features.nav_menu_secante.PasoSecante
import com.example.mathsus_ia.ui.features.BisectionScreen
import com.example.mathsus_ia.ui.features.FalsiScreen
import com.example.mathsus_ia.ui.features.Info
import com.example.mathsus_ia.ui.features.NewtonScreen
import com.example.mathsus_ia.ui.features.SecanteScreen
import com.example.mathsus_ia.ui.features.SplashScreen
import com.example.mathsus_ia.ui.features.nav_menu_newton.ExerciseNewton
import com.example.mathsus_ia.ui.features.nav_menu_newton.InformationNewton
import com.example.mathsus_ia.ui.features.nav_menu_newton.PasoNewton
import com.example.mathsus_ia.ui.theme.MATHSUS_IATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MATHSUS_IATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable(route = "splash") { SplashScreen(navController = navController) }

                        composable(route = "bisection") { BisectionScreen(navController = navController) }
                        composable(route = "pasoBisection") { PasoBisection(navController = navController) }
                        composable(route = "infoBisection") { InformationBisection(navController = navController) }
                        composable(route = "exerciseBisection") { ExerciseBisection(navController = navController) }

                        composable(route = "newton") { NewtonScreen(navController = navController) }
                        composable(route = "pasoNewton") { PasoNewton(navController = navController) }
                        composable(route = "infoNewton") { InformationNewton(navController = navController) }
                        composable(route = "exerciseNewton") { ExerciseNewton(navController = navController) }

                        composable(route = "secante") {SecanteScreen(navController = navController) }
                        composable(route = "pasoSecante") { PasoSecante(navController = navController) }
                        composable(route = "infoSecante") { InformationSecante(navController = navController) }
                        composable(route = "exerciseSecante") { ExerciseSecante(navController = navController) }

                        composable(route = "falsi") {FalsiScreen(navController = navController) }
                        composable(route = "pasoFalsi") { PasoFalsi(navController = navController) }
                        composable(route = "infoFalsi") { InformationFalsi(navController = navController) }
                        composable(route = "exerciseFalsi") {ExerciseFalsi(navController = navController) }

                        composable(route = "info") { Info(navController = navController) }

                        composable(route = "baking") { BakingScreen(navController = navController) }

                    }
                    //BakingScreen()
                }
            }
        }
    }
}

/*
RADICADO:  20252100001592492
 */