package com.vigovlugt.fiveenative.db

import app.cash.sqldelight.db.SqlDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        throw NotImplementedError("Database is not supported on web yet")
}
