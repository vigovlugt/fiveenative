package com.vigovlugt.fiveenative.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vigovlugt.fiveenative.resources.Res
import com.vigovlugt.fiveenative.resources.ic_article
import com.vigovlugt.fiveenative.resources.ic_search
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
) {
    Content("Content", Res.drawable.ic_article, ContentRoute, ContentRoute::class),
    Search("Search", Res.drawable.ic_search, SearchRoute, SearchRoute::class),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen() {
    val tabNavController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fiveenative") })
        },
        bottomBar = {
            NavigationBar {
                val currentDestination =
                    tabNavController.currentBackStackEntryAsState().value?.destination
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(tab.routeClass)
                        } == true,
                        onClick = {
                            tabNavController.navigate(tab.route) {
                                popUpTo(tabNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            navController = tabNavController,
            startDestination = ContentRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable<ContentRoute> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No backgrounds yet.")
                }
            }
            composable<SearchRoute> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Search", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
