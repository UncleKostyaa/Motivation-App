package com.unclekostya.motivationapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unclekostya.motivationapp.presentation.favourites.FavouritesScreen
import com.unclekostya.motivationapp.presentation.quote.QuoteScreen
import com.unclekostya.motivationapp.presentation.quote.QuoteViewModel
import com.unclekostya.motivationapp.presentation.settings.SettingsPage


@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: QuoteViewModel,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            QuoteScreen(viewModel = viewModel)
        }
        composable("settings") {
            SettingsPage(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
        composable("favourites") {
            FavouritesScreen()
        }
    }
}
