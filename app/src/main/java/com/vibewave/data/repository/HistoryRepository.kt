package com.vibewave.data.repository

import com.vibewave.data.db.HistoryDao
import com.vibewave.data.db.HistoryEntity
import com.vibewave.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Records every track the user plays and exposes flows for the profile screen.
 *
 * On each play we upsert the track: if it's new we insert it with playCount=1,
 * otherwise we increment playCount and refresh lastPlayedAt.
 */
@Singleton
class HistoryRepository @Inject constructor(
    private val dao: HistoryDao,
) {

    val recent: Flow<List<Track>> =
        dao.observeRecent().map { list -> list.map(HistoryEntity::toDomain) }

    val top: Flow<List<Track>> =
        dao.observeTop().map { list -> list.map(HistoryEntity::toDomain) }

    val totalCount: Flow<Int> = dao.observeCount()

    suspend fun recordPlay(track: Track, nowMs: Long = System.currentTimeMillis()) {
        val existing = dao.findById(track.id)
        val newCount = (existing?.playCount ?: 0) + 1
        dao.upsert(HistoryEntity.fromTrack(track, nowMs, newCount))
    }

    suspend fun clearAll() = dao.clear()
}
