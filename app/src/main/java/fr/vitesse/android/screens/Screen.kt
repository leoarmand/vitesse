package fr.vitesse.android.screens

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object CandidateDetails : Screen(
        route = "candidateDetails/{candidateId}",
        navArguments = listOf(navArgument("candidateId") {
            type = NavType.LongType
        })
    ) {
        fun createRoute(candidateId: Long) = "candidateDetails/$candidateId"
    }

    data object EditCandidate : Screen(
        route = "editCandidate/{candidateId}",
        navArguments = listOf(navArgument("candidateId") {
            type = NavType.LongType
        })
    ) {
        fun createRoute(candidateId: Long?) = "createOrEditAnimal/$candidateId"
    }

    data object CreateCandidate : Screen("createCandidate")
}
