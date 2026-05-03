package com.vibewave.data.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.vibewave.domain.model.Track
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────────────────────
// History — plays ordered by time + play count
// ─────────────────────────────────────────────────────────────────────────────

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey val trackId: Long,
    val title: String,
    val artist: String,
    val albumTitle: String,
    val albumArt: String?,
    val previewUrl: String,
    val durationSec: Int,
    val explicit: Boolean,
    val lastPlayedAt: Long,
    val playCount: Int,
) {
    fun toDomain(): Track = Track(
        id = trackId, title = title, artist = artist,
        albumTitle = albumTitle, albumArt = albumArt,
        previewUrl = previewUrl, durationSec = durationSec,
        explicit = explicit,
    )

    companion object {
        fun fromTrack(t: Track, nowMs: Long, playCount: Int) = HistoryEntity(
            trackId = t.id, title = t.title, artist = t.artist,
            albumTitle = t.albumTitle, albumArt = t.albumArt,
            previewUrl = t.previewUrl, durationSec = t.durationSec,
            explicit = t.explicit,
            lastPlayedAt = nowMs, playCount = playCount,
        )
    }
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int = 200): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history ORDER BY playCount DESC, lastPlayedAt DESC LIMIT :limit")
    fun observeTop(limit: Int = 20): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE trackId = :id LIMIT 1")
    suspend fun findById(id: Long): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM history")
    fun observeCount(): Flow<Int>
}

// ─────────────────────────────────────────────────────────────────────────────
// Favorites — tracks the user has "liked"
// ─────────────────────────────────────────────────────────────────────────────

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val trackId: Long,
    val title: String,
    val artist: String,
    val albumTitle: String,
    val albumArt: String?,
    val previewUrl: String,
    val durationSec: Int,
    val explicit: Boolean,
    val addedAt: Long,           // epoch millis, newest first
) {
    fun toDomain(): Track = Track(
        id = trackId, title = title, artist = artist,
        albumTitle = albumTitle, albumArt = albumArt,
        previewUrl = previewUrl, durationSec = durationSec,
        explicit = explicit,
    )

    companion object {
        fun fromTrack(t: Track, nowMs: Long) = FavoriteEntity(
            trackId = t.id, title = t.title, artist = t.artist,
            albumTitle = t.albumTitle, albumArt = t.albumArt,
            previewUrl = t.previewUrl, durationSec = t.durationSec,
            explicit = t.explicit,
            addedAt = nowMs,
        )
    }
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteEntity>>

    /** Emits whenever the given trackId's liked status changes. */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE trackId = :id)")
    fun observeIsFavorite(id: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE trackId = :id)")
    suspend fun isFavorite(id: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE trackId = :id")
    suspend fun remove(id: Long)

    @Query("SELECT COUNT(*) FROM favorites")
    fun observeCount(): Flow<Int>
}

// ─────────────────────────────────────────────────────────────────────────────
// Database
// ─────────────────────────────────────────────────────────────────────────────

@Database(
    entities = [HistoryEntity::class, FavoriteEntity::class],
    version = 2,                       // bumped because we added a table
    exportSchema = false,
)
abstract class VibeWaveDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao
}
