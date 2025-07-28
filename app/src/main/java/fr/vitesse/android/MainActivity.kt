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
import fr.vitesse.android.screens.HomeScreen
import fr.vitesse.android.screens.Screen
import fr.vitesse.android.service.CandidateService
import fr.vitesse.android.ui.theme.VitesseTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val candidateService: CandidateService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            candidateService.initIfNeeded()

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
                    }
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
        }
    }
}
