package com.vibewave.data.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.vibewave.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads local audio tracks from the device through MediaStore.
 *
 * Filters:
 *  • Only tracks marked as music by the system (IS_MUSIC = 1)
 *  • Only files inside /Music or /Download
 *  • Duration ≥ 10 seconds (eliminates ringtones/notifications/sound effects)
 *  • Excludes anything in /Ringtones, /Notifications, /Alarms folders
 *
 * Album art is delivered as a content:// URI built from the album_id.
 * Tracks without embedded art get null — UI shows a placeholder.
 */
@Singleton
class LocalMediaRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /** Loads all matching tracks. Safe to call from any dispatcher. */
    suspend fun loadAll(): List<Track> = withContext(Dispatchers.IO) {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.IS_MUSIC,
        )

        // Min 10 sec, must be music
        val selection = buildString {
            append("${MediaStore.Audio.Media.IS_MUSIC} = 1")
            append(" AND ${MediaStore.Audio.Media.DURATION} >= 10000")
        }

        val sortOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"

        val results = mutableListOf<Track>()
        context.contentResolver.query(
            collection, projection, selection, null, sortOrder,
        )?.use { cursor ->
            val idCol       = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol    = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol   = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol    = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdCol  = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol     = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                val path = cursor.getString(dataCol) ?: continue

                // Path filter: /Music or /Download only.
                // Reject Ringtones / Notifications / Alarms even if classified
                // as music by some buggy phones.
                val lower = path.lowercase()
                val isInMusicOrDownload =
                    lower.contains("/music/") || lower.contains("/download/")
                val isSystemSound =
                    lower.contains("/ringtones/") ||
                    lower.contains("/notifications/") ||
                    lower.contains("/alarms/")

                if (!isInMusicOrDownload || isSystemSound) continue

                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol) ?: "Unknown"
                val artist = cursor.getString(artistCol)?.takeIf { it.isNotBlank() && it != "<unknown>" }
                    ?: "Unknown artist"
                val album = cursor.getString(albumCol).orEmpty()
                val albumId = cursor.getLong(albumIdCol)
                val durationMs = cursor.getLong(durationCol)

                val trackUri = ContentUris.withAppendedId(collection, id)
                val artUri = ContentUris.withAppendedId(
                    android.net.Uri.parse("content://media/external/audio/albumart"),
                    albumId,
                )

                results += Track(
                    id = -id,                            // negative IDs avoid collisions with Deezer's
                    title = title,
                    artist = artist,
                    albumTitle = album,
                    albumArt = artUri.toString(),        // resolver returns null for tracks w/o art
                    previewUrl = trackUri.toString(),    // file/content URI plays directly via Media3
                    durationSec = (durationMs / 1000L).toInt(),
                    explicit = false,
                )
            }
        }
        results
    }
}
