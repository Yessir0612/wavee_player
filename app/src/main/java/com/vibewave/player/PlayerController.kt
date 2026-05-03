package com.vibewave.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.vibewave.data.repository.HistoryRepository
import com.vibewave.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * High-level facade over Media3.
 *
 * The UI collects flows like [currentTrack], [isPlaying], [progressMs] and
 * calls commands like [play], [toggle], [next], [prev], [seekTo]. All the
 * MediaController/MediaItem boilerplate is hidden here; the same instance
 * is shared app-wide via Hilt's @Singleton.
 */
@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val historyRepo: HistoryRepository,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var controller: MediaController? = null

    // ── Observable state ─────────────────────────────────────────────────────

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    val queue: StateFlow<List<Track>> = _queue.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _progressMs = MutableStateFlow(0L)
    val progressMs: StateFlow<Long> = _progressMs.asStateFlow()

    private val _durationMs = MutableStateFlow(30_000L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _shuffle = MutableStateFlow(false)
    val shuffle: StateFlow<Boolean> = _shuffle.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    // ── Lifecycle ────────────────────────────────────────────────────────────

    /** Called from the activity's onStart(). Safe to call multiple times. */
    fun connect() {
        if (controller != null) return
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener({
            controller = future.get().also(::attachListener)
            syncFromPlayer()
        }, MoreExecutors.directExecutor())
    }

    fun release() {
        controller?.release()
        controller = null
    }

    // ── Commands ─────────────────────────────────────────────────────────────

    /**
     * Play a track. If [queue] is empty we use the full last-shown list as
     * the queue so that next/prev work; otherwise the provided queue wins.
     *
     * [REPEAT_MODE_ALL] by default so the final track wraps to the first —
     * this also makes `next` cyclic which is what users expect from a player.
     */
    fun play(track: Track, queue: List<Track> = listOf(track)) {
        val c = controller ?: return
        val finalQueue = if (queue.isEmpty()) listOf(track) else queue
        _queue.value = finalQueue

        val startIndex = finalQueue.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        c.setMediaItems(finalQueue.map { it.toMediaItem() }, startIndex, 0L)
        c.repeatMode = Player.REPEAT_MODE_ALL
        c.prepare()
        c.play()

        _currentTrack.value = track
        scope.launch { historyRepo.recordPlay(track) }
    }

    fun toggle() {
        val c = controller ?: return
        if (c.isPlaying) c.pause() else c.play()
    }

    /**
     * Jump to the next track. Falls back to manual wrap-around if the
     * player's own seekToNextMediaItem is unavailable (single-track queue,
     * or controller not yet ready).
     */
    fun next() {
        val c = controller ?: return
        val q = _queue.value
        if (q.size <= 1) return

        if (c.hasNextMediaItem()) {
            c.seekToNextMediaItem()
        } else {
            // Wrap around manually
            c.seekTo(0, 0L)
        }
        // currentTrack gets updated by the onMediaItemTransition listener
    }

    /**
     * Go to previous track. Within the first 3 seconds of a track, pressing
     * prev restarts the current track instead (standard UX).
     */
    fun prev() {
        val c = controller ?: return
        if (c.currentPosition > 3_000) {
            c.seekTo(0)
            return
        }
        val q = _queue.value
        if (q.size <= 1) { c.seekTo(0); return }

        if (c.hasPreviousMediaItem()) {
            c.seekToPreviousMediaItem()
        } else {
            // Wrap around to end
            c.seekTo(q.lastIndex, 0L)
        }
    }

    fun seekTo(ms: Long) = controller?.seekTo(ms)

    fun toggleShuffle() {
        val c = controller ?: return
        c.shuffleModeEnabled = !c.shuffleModeEnabled
    }

    fun cycleRepeat() {
        val c = controller ?: return
        c.repeatMode = when (c.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
    }

    // ── Wiring ───────────────────────────────────────────────────────────────

    private fun attachListener(c: MediaController) {
        c.addListener(object : Player.Listener {
            override fun onMediaItemTransition(item: MediaItem?, reason: Int) {
                syncTrack()
                // Any time the track changes — including auto-advance — record
                // the new one in history.
                _currentTrack.value?.let { track ->
                    scope.launch { historyRepo.recordPlay(track) }
                }
            }
            override fun onIsPlayingChanged(p: Boolean) { _isPlaying.value = p }
            override fun onPlaybackStateChanged(state: Int) {
                _isBuffering.value = state == Player.STATE_BUFFERING
                _durationMs.value = c.duration.coerceAtLeast(1)
            }
            override fun onShuffleModeEnabledChanged(enabled: Boolean) { _shuffle.value = enabled }
            override fun onRepeatModeChanged(mode: Int) { _repeatMode.value = mode }
        })

        // 5x/sec progress ticker — cheap and lets the UI animate smoothly
        scope.launch {
            while (true) {
                controller?.let {
                    _progressMs.value = it.currentPosition.coerceAtLeast(0)
                    _durationMs.value = it.duration.coerceAtLeast(1)
                }
                kotlinx.coroutines.delay(200)
            }
        }
    }

    private fun syncFromPlayer() {
        val c = controller ?: return
        _isPlaying.value = c.isPlaying
        _shuffle.value = c.shuffleModeEnabled
        _repeatMode.value = c.repeatMode
        syncTrack()
    }

    private fun syncTrack() {
        val c = controller ?: return
        val trackId = c.currentMediaItem?.mediaId?.toLongOrNull() ?: return
        _currentTrack.value = _queue.value.firstOrNull { it.id == trackId }
            ?: _currentTrack.value   // keep previous if not found (e.g., shuffle)
    }

    private fun Track.toMediaItem(): MediaItem = MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(previewUrl)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artist)
                .setAlbumTitle(albumTitle)
                .setArtworkUri(albumArt?.let(android.net.Uri::parse))
                .build()
        )
        .build()
}
