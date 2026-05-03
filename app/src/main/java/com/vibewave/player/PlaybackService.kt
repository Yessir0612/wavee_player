package com.vibewave.player

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.vibewave.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * The foreground service that owns the ExoPlayer instance.
 *
 * Android's Media3 [MediaSessionService] takes care of:
 *   • Displaying the media notification (skip/play/pause buttons)
 *   • Routing audio-focus, Bluetooth, and hardware keys
 *   • Surviving when the app is backgrounded
 *
 * The UI layer never touches ExoPlayer directly — it goes through
 * [PlayerController], which owns a [MediaController] connected to this service.
 */
@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            .setHandleAudioBecomingNoisy(true)   // pause when headphones unplugged
            .build()

        // Tapping the notification returns to MainActivity.
        val sessionActivityPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        // If the user swipes the app away while nothing is playing, stop the service.
        val player = mediaSession?.player ?: return
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
