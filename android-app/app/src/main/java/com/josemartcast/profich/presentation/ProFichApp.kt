package com.josemartcast.profich.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josemartcast.profich.presentation.about.AboutScreen
import com.josemartcast.profich.presentation.home.HomeScreen
import com.josemartcast.profich.presentation.home.HomeViewModel
import com.josemartcast.profich.presentation.navigation.ProFichDestination

@Composable
fun ProFichApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ProFichDestination.start.route,
    ) {
        composable(ProFichDestination.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = uiState,
                onAboutClick = {
                    navController.navigate(ProFichDestination.About.route)
                },
            )
        }
        composable(ProFichDestination.About.route) {
            AboutScreen(onBackClick = navController::navigateUp)
        }
    }
}
