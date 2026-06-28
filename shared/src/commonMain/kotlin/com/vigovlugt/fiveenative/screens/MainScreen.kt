package com.vigovlugt.fiveenative.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vigovlugt.fiveenative.db.Database
import com.vigovlugt.fiveenative.resources.Res
import com.vigovlugt.fiveenative.resources.ic_article
import com.vigovlugt.fiveenative.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private enum class Tab(val label: String, val icon: DrawableResource) {
    Content("Content", Res.drawable.ic_article),
    Search("Search", Res.drawable.ic_search),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    database: Database,
    onEditSource: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(Tab.Content) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fiveenative") },
                actions = {
                    TextButton(onClick = onEditSource) { Text("Source") }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (selectedTab) {
                Tab.Content -> BackgroundsList(database)
                Tab.Search -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Search", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}

@Composable
private fun BackgroundsList(database: Database) {
    val backgrounds = database.databaseQueries.selectAllBackgrounds().executeAsList()

    if (backgrounds.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("No backgrounds yet.")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(backgrounds) { background ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(background.name, style = MaterialTheme.typography.titleMedium)
                background.source?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
            HorizontalDivider()
        }
    }
}
