package com.vigovlugt.fiveenative

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vigovlugt.fiveenative.resources.Res
import com.vigovlugt.fiveenative.resources.ic_article
import com.vigovlugt.fiveenative.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private enum class Tab(val label: String, val icon: DrawableResource) {
    Content("Content", Res.drawable.ic_article),
    Search("Search", Res.drawable.ic_search),
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var selectedTab by remember { mutableStateOf(Tab.Content) }

        Scaffold(
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
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                when (selectedTab) {
                    Tab.Content -> Text("Content", style = MaterialTheme.typography.headlineMedium)
                    Tab.Search -> Text("Search", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
