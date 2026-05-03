package com.vibewave.domain.model

/**
 * Domain track — intentionally decoupled from the Deezer DTO.
 * Swapping audio providers only requires a new mapper.
 */
data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val albumTitle: String,
    val albumArt: String?,     // best available size
    val previewUrl: String,    // 30-second MP3
    val durationSec: Int,
    val explicit: Boolean = false,
) {
    /** "1:54", "0:30", etc. */
    val durationFormatted: String
        get() = "%d:%02d".format(durationSec / 60, durationSec % 60)
}
