package com.vigovlugt.fiveenative

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vigovlugt.fiveenative.db.Database
import com.vigovlugt.fiveenative.screens.MainScreen
import com.vigovlugt.fiveenative.screens.SourceScreen

@Composable
@Preview
fun App(database: Database? = null) {
    MaterialTheme {
        if (database == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Data is not available on web yet.")
            }
            return@MaterialTheme
        }

        var showSource by remember {
            mutableStateOf(database.databaseQueries.getSource().executeAsOneOrNull() == null)
        }

        if (showSource) {
            SourceScreen(
                database = database,
                onSaved = { showSource = false },
            )
        } else {
            MainScreen(
                database = database,
                onEditSource = { showSource = true },
            )
        }
    }
}
