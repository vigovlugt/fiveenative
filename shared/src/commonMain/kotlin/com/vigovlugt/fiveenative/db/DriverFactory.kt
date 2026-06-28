package com.vigovlugt.fiveenative.db

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(factory: DriverFactory): Database = Database(factory.createDriver())
