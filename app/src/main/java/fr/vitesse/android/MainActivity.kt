package fr.vitesse.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.vitesse.android.screens.CandidateDetailsScreen
import fr.vitesse.android.screens.CreateCandidateScreen
import fr.vitesse.android.screens.HomeScreen
import fr.vitesse.android.screens.Screen
import fr.vitesse.android.ui.theme.VitesseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            enableEdgeToEdge()
            setContent {
                val navController = rememberNavController()
                VitesseTheme {
                    VitesseNavHost(navController)
                }
            }
        }
    }

    @Composable
    fun VitesseNavHost(navHostController: NavHostController) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.Home.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    modifier = Modifier,
                    onCandidateClick = {
                        id ->
                        navHostController.navigate(
                            Screen.CandidateDetails.createRoute(
                                candidateId = id
                            )
                        )
                    },
                    onAddCandidateClick = { navHostController.navigate(Screen.CreateCandidate.route) },
                )
            }
            composable(
                route = Screen.CandidateDetails.route,
                arguments = Screen.CandidateDetails.navArguments
            ) { backStackEntry ->
                CandidateDetailsScreen(
                    backStackEntry = backStackEntry,
                    onBackClick = { navHostController.navigateUp() }
                )
            }
            composable(route = Screen.CreateCandidate.route
            ) { backStackEntry ->
                CreateCandidateScreen(
                    backStackEntry = backStackEntry,
                    onBackClick = { navHostController.navigateUp() },
                    onSaveClick = { navHostController.navigateUp() }
                )
            }
            composable(
                route = Screen.EditCandidate.route,
                arguments = Screen.CandidateDetails.navArguments
            ) { backStackEntry ->
                CreateCandidateScreen (
                    backStackEntry = backStackEntry,
                    onBackClick = { navHostController.navigateUp() },
                    onSaveClick = {  }
                )
            }
        }
    }
}
