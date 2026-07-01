package com.vigovlugt.fiveenative.screens.content

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vigovlugt.fiveenative.ContentRoute

internal fun NavHostController.navigateToBackgrounds() {
    navigate(BackgroundsRoute)
}

internal fun NavGraphBuilder.contentGraph(
    onNavigateToBackgrounds: () -> Unit,
) {
    navigation<ContentRoute>(startDestination = ContentHomeRoute) {
        composable<ContentHomeRoute> {
            ContentHomeScreen(onBackgroundsClick = onNavigateToBackgrounds)
        }
        composable<BackgroundsRoute> { BackgroundsScreen() }
    }
}
