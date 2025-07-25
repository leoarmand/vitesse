package fr.vitesse.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.vitesse.android.module.CandidateActionComposerModule
import fr.vitesse.android.screens.CandidateDetailsScreen
import fr.vitesse.android.screens.HomeScreen
import fr.vitesse.android.screens.Screen
import fr.vitesse.android.service.CandidateService
import fr.vitesse.android.ui.theme.VitesseTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var candidateService: CandidateService

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
                val context = LocalContext.current
                val sendToComposer = remember {
                    CandidateActionComposerModule(context)
                }
                CandidateDetailsScreen(
                    sendToComposer = sendToComposer,
                    backStackEntry = backStackEntry,
                    onBackClick = { navHostController.navigateUp() }
                )
            }
        }
    }
}
