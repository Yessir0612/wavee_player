package com.vibewave.data.model

import com.vibewave.domain.model.Track
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── API response containers ───────────────────────────────────────────────────

@Serializable
data class DeezerListResponse(
    @SerialName("data") val data: List<TrackDto> = emptyList(),
    @SerialName("total") val total: Int = 0,
    @SerialName("next") val next: String? = null,
)

@Serializable
data class DeezerChartResponse(
    @SerialName("tracks") val tracks: DeezerListResponse = DeezerListResponse(),
)

// ── Track DTO ─────────────────────────────────────────────────────────────────

@Serializable
data class TrackDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("duration") val duration: Int,
    @SerialName("preview") val preview: String,
    @SerialName("artist") val artist: ArtistDto,
    @SerialName("album") val album: AlbumDto,
    @SerialName("explicit_lyrics") val explicit: Boolean = false,
)

@Serializable
data class ArtistDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("picture_medium") val pictureMedium: String? = null,
)

@Serializable
data class AlbumDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("cover_medium") val coverMedium: String? = null,
    @SerialName("cover_big") val coverBig: String? = null,
    @SerialName("cover_xl") val coverXl: String? = null,
)

// ── DTO → domain mapper ──────────────────────────────────────────────────────

fun TrackDto.toDomain(): Track = Track(
    id = id,
    title = title,
    artist = artist.name,
    albumTitle = album.title,
    albumArt = album.coverXl ?: album.coverBig ?: album.coverMedium,
    previewUrl = preview,
    durationSec = duration,
    explicit = explicit,
)
