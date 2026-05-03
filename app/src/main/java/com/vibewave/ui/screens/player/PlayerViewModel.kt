package com.vibewave.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.data.repository.FavoritesRepository
import com.vibewave.domain.model.Track
import com.vibewave.player.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI facade over PlayerController. Exposes all playback state plus
 * the "is the currently-playing track favorited?" flow for the heart button.
 */
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: PlayerController,
    private val favorites: FavoritesRepository,
) : ViewModel() {

    val currentTrack: StateFlow<Track?> = player.currentTrack
    val isPlaying = player.isPlaying
    val isBuffering = player.isBuffering
    val progressMs = player.progressMs
    val durationMs = player.durationMs
    val shuffle = player.shuffle
    val repeatMode = player.repeatMode
    val queue = player.queue

    /** 0f..1f — ready for LinearProgressIndicator. */
    val progressFraction: StateFlow<Float> = combine(progressMs, durationMs) { p, d ->
        if (d <= 0) 0f else (p.toFloat() / d.toFloat()).coerceIn(0f, 1f)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    /** True iff the current track is liked. Recomputes whenever the track changes. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val isCurrentFavorite: StateFlow<Boolean> = currentTrack
        .flatMapLatest { track ->
            if (track == null) flowOf(false)
            else favorites.observeIsFavorite(track.id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    // ── Commands ─────────────────────────────────────────────────────────────

    fun play(track: Track, queue: List<Track> = listOf(track)) = player.play(track, queue)
    fun toggle() = player.toggle()
    fun next() = player.next()
    fun prev() = player.prev()
    fun seekTo(ms: Long) = player.seekTo(ms)
    fun seekFraction(f: Float) {
        val d = durationMs.value
        player.seekTo((d * f).toLong())
    }
    fun toggleShuffle() = player.toggleShuffle()
    fun cycleRepeat() = player.cycleRepeat()

    fun toggleFavorite() {
        val track = currentTrack.value ?: return
        viewModelScope.launch { favorites.toggle(track) }
    }
}
