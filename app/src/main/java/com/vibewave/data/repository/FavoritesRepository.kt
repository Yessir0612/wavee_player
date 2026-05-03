package com.vibewave.data.repository

import com.vibewave.data.db.FavoriteDao
import com.vibewave.data.db.FavoriteEntity
import com.vibewave.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Like/unlike tracks and observe the resulting list.
 *
 * Each Track is fully denormalized into the [FavoriteEntity] so the
 * favorites screen stays functional offline — no need to re-query Deezer.
 */
@Singleton
class FavoritesRepository @Inject constructor(
    private val dao: FavoriteDao,
) {

    val all: Flow<List<Track>> =
        dao.observeAll().map { list -> list.map(FavoriteEntity::toDomain) }

    val count: Flow<Int> = dao.observeCount()

    fun observeIsFavorite(trackId: Long): Flow<Boolean> = dao.observeIsFavorite(trackId)

    suspend fun isFavorite(trackId: Long): Boolean = dao.isFavorite(trackId)

    suspend fun add(track: Track, nowMs: Long = System.currentTimeMillis()) {
        dao.insert(FavoriteEntity.fromTrack(track, nowMs))
    }

    suspend fun remove(trackId: Long) = dao.remove(trackId)

    /** Flip the like state and return the new value. */
    suspend fun toggle(track: Track): Boolean {
        val nowLiked = !dao.isFavorite(track.id)
        if (nowLiked) add(track) else remove(track.id)
        return nowLiked
    }
}
