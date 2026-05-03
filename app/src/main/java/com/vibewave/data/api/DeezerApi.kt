package com.vibewave.data.api

import com.vibewave.data.model.DeezerChartResponse
import com.vibewave.data.model.DeezerListResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Public Deezer API — no auth key required.
 *
 *   • GET /search?q=... → tracks matching the query
 *   • GET /chart        → global top tracks
 */
@Singleton
class DeezerApi @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun search(query: String, limit: Int = 50): DeezerListResponse =
        client.get("$BASE/search") {
            parameter("q", query)
            parameter("limit", limit)
        }.body()

    suspend fun chart(): DeezerChartResponse =
        client.get("$BASE/chart").body()

    private companion object {
        const val BASE = "https://api.deezer.com"
    }
}
