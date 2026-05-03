package com.vibewave.data.repository

import com.vibewave.core.result.Outcome
import com.vibewave.data.api.DeezerApi
import com.vibewave.data.model.toDomain
import com.vibewave.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fetches tracks from Deezer. All network calls are dispatched on IO.
 *
 * Wraps results in [Outcome] so callers don't need try/catch themselves —
 * errors become first-class UI state.
 */
@Singleton
class MusicRepository @Inject constructor(
    private val api: DeezerApi,
) {
    suspend fun search(query: String): Outcome<List<Track>> = runIo {
        api.search(query).data.map { it.toDomain() }
    }

    suspend fun chart(): Outcome<List<Track>> = runIo {
        api.chart().tracks.data.map { it.toDomain() }
    }

    private suspend inline fun <T> runIo(crossinline block: suspend () -> T): Outcome<T> =
        withContext(Dispatchers.IO) {
            try {
                Outcome.Success(block())
            } catch (t: Throwable) {
                Outcome.Error(t.localizedMessage ?: "Network error", t)
            }
        }
}
