package fr.vitesse.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.vitesse.android.data.Candidate
import fr.vitesse.android.screens.CandidateDetailsScreen
import fr.vitesse.android.screens.HomeScreen
import fr.vitesse.android.screens.Screen
import fr.vitesse.android.service.CandidateService
import fr.vitesse.android.ui.theme.VitesseTheme
import fr.vitesse.android.viewmodel.CandidateDetailsViewModel
import fr.vitesse.android.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val homeViewModel: HomeViewModel by viewModels()
    val candidateDetailsViewModel: CandidateDetailsViewModel by viewModels()
    @Inject lateinit var candidateService: CandidateService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            candidateService.initIfNeeded()

            enableEdgeToEdge()
            setContent {
                val navController = rememberNavController()
                VitesseTheme {
                    VitesseNavHost(navController, homeViewModel.candidates.value)
                }
            }
        }
    }

    @Composable
    fun VitesseNavHost(navHostController: NavHostController, candidates: List<Candidate>) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.Home.route
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    modifier = Modifier,
                    candidates,
                    /*onCandidateClick = navHostController.navigate(
                        Screen.CandidateDetails.createRoute(
                            candidateId = it.id
                        )
                    )*/
                )
            }
            composable(
                route = Screen.CandidateDetails.route,
                arguments = Screen.CandidateDetails.navArguments
            ) { backStackEntry ->
                CandidateDetailsScreen(
                    candidateDetailsViewModel = candidateDetailsViewModel,
                    onBackClick = { navHostController.navigateUp() },
                    onDeleteClick = { navHostController.navigateUp() },
                    onEditClick = { navHostController.navigateUp() }
                )
            }
        }
    }
}
