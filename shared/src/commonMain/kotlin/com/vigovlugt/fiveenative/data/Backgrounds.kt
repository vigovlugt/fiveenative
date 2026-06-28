package com.vigovlugt.fiveenative.data

import com.vigovlugt.fiveenative.db.Database
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
private data class BackgroundsResponse(
    val background: List<JsonObject> = emptyList(),
)

suspend fun downloadBackgrounds(database: Database, sourceUrl: String) {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    try {
        val url = sourceUrl.trimEnd('/') + "/data/backgrounds.json"
        val response = client.get(url) {
            // 5e.tools is behind Cloudflare and returns a 403 challenge page to
            // non-browser clients, so present a browser-like User-Agent.
            header(
                HttpHeaders.UserAgent,
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/124.0 Safari/537.36",
            )
        }.body<BackgroundsResponse>()
        database.databaseQueries.transaction {
            database.databaseQueries.deleteAllBackgrounds()
            response.background.forEach { background ->
                database.databaseQueries.insertBackground(
                    name = background["name"]?.jsonPrimitive?.content ?: "Unknown",
                    source = background["source"]?.jsonPrimitive?.content,
                    json = background.toString(),
                )
            }
        }
    } finally {
        client.close()
    }
}
