package com.josemartcast.profich.presentation.navigation

sealed interface ProFichDestination {
    val route: String

    data object Home : ProFichDestination {
        override val route = "home"
    }

    data object About : ProFichDestination {
        override val route = "about"
    }

    companion object {
        val start: ProFichDestination = Home
    }
}
