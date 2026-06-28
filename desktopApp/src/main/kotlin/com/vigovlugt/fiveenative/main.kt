package com.vigovlugt.fiveenative

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vigovlugt.fiveenative.db.DriverFactory
import com.vigovlugt.fiveenative.db.createDatabase

fun main() = application {
    val database = createDatabase(DriverFactory())

    Window(
        onCloseRequest = ::exitApplication,
        title = "Fiveenative",
    ) {
        App(database)
    }
}