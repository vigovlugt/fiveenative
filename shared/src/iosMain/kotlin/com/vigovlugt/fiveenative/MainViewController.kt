package com.vigovlugt.fiveenative

import androidx.compose.ui.window.ComposeUIViewController
import com.vigovlugt.fiveenative.db.DriverFactory
import com.vigovlugt.fiveenative.db.createDatabase

fun MainViewController() = ComposeUIViewController {
    App(createDatabase(DriverFactory()))
}