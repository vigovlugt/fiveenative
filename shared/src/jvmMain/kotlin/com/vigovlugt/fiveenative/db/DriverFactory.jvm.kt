package com.vigovlugt.fiveenative.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:fiveenative.db")
        val current = Database.Schema.version
        val onDisk = driver.executeQuery(
            null,
            "PRAGMA user_version;",
            { cursor -> app.cash.sqldelight.db.QueryResult.Value(if (cursor.next().value) cursor.getLong(0) else 0L) },
            0,
        ).value ?: 0L
        if (onDisk == 0L) {
            Database.Schema.create(driver)
            driver.execute(null, "PRAGMA user_version = $current;", 0)
        }
        return driver
    }
}
