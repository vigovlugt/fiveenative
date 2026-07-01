package com.vigovlugt.fiveenative

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vigovlugt.fiveenative.resources.Res
import com.vigovlugt.fiveenative.resources.ic_arrow_back
import com.vigovlugt.fiveenative.resources.ic_article
import com.vigovlugt.fiveenative.resources.ic_search
import com.vigovlugt.fiveenative.screens.SearchScreen
import com.vigovlugt.fiveenative.screens.content.ContentHomeRoute
import com.vigovlugt.fiveenative.screens.content.contentGraph
import com.vigovlugt.fiveenative.screens.content.navigateToBackgrounds
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.reflect.KClass

@Serializable
object ContentRoute

@Serializable
object SearchRoute

private enum class Tab(
    val label: String,
    val icon: DrawableResource,
    val route: Any,
    val routeClass: KClass<*>,
    val rootRoute: Any,
    val rootRouteClass: KClass<*>,
) {
    Content(
        "Content",
        Res.drawable.ic_article,
        ContentRoute,
        ContentRoute::class,
        ContentHomeRoute,
        ContentHomeRoute::class,
    ),
    Search(
        "Search",
        Res.drawable.ic_search,
        SearchRoute,
        SearchRoute::class,
        SearchRoute,
        SearchRoute::class,
    ),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val currentDestination =
            navController.currentBackStackEntryAsState().value?.destination
        val selectedTab = Tab.entries.find { tab ->
            currentDestination?.hierarchy?.any { it.hasRoute(tab.routeClass) } == true
        }
        val showBackButton = selectedTab != null &&
            currentDestination?.hasRoute(selectedTab.rootRouteClass) != true

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Fiveenative") },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_arrow_back),
                                    contentDescription = "Back",
                                )
                            }
                        }
                    },
                )
            },
            bottomBar = {
                NavigationBar {
                    Tab.entries.forEach { tab ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(tab.routeClass)
                        } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    navController.navigate(tab.rootRoute) {
                                        popUpTo(tab.route) { saveState = true }
                                        launchSingleTop = true
                                    }
                                } else {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(tab.icon),
                                    contentDescription = tab.label,
                                )
                            },
                            label = { Text(tab.label) },
                        )
                    }
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ContentRoute,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                contentGraph(onNavigateToBackgrounds = navController::navigateToBackgrounds)
                composable<SearchRoute> { SearchScreen() }
            }
        }
    }
}
