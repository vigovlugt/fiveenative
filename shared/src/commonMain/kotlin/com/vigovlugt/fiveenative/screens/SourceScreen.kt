package com.vigovlugt.fiveenative.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vigovlugt.fiveenative.data.downloadBackgrounds
import com.vigovlugt.fiveenative.db.Database
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SourceScreen(
    database: Database,
    onSaved: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val existingUrl = remember { database.databaseQueries.getSource().executeAsOneOrNull() }
    var url by remember { mutableStateOf(existingUrl.orEmpty()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Data source") }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                "Set the base URL of the data source. " +
                    "For example, https://5e.tools/ downloads from https://5e.tools/data/backgrounds.json.",
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Source URL") },
                singleLine = true,
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
            )
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Button(
                enabled = !loading && url.isNotBlank(),
                onClick = {
                    val target = url.trim()
                    error = null
                    loading = true
                    scope.launch {
                        try {
                            database.databaseQueries.setSource(target)
                            downloadBackgrounds(database, target)
                            onSaved()
                        } catch (e: Exception) {
                            error = e.message ?: "Failed to download data"
                        } finally {
                            loading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                    Text("Downloading...")
                } else {
                    Text("Save and download")
                }
            }
            if (existingUrl != null) {
                TextButton(
                    enabled = !loading,
                    onClick = onSaved,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}
