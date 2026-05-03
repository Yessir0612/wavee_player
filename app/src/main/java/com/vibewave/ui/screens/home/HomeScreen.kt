package com.vibewave.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vibewave.domain.model.Track
import com.vibewave.R
import com.vibewave.ui.components.ShimmerTrackRow
import com.vibewave.ui.components.TrackRow
import com.vibewave.ui.screens.player.PlayerViewModel
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * Home — horizontal "Recently played" strip on top (live from Room),
 * then a "Trending now" vertical list (from Deezer /chart).
 */
@Composable
fun HomeScreen(
    onTrackClick: (Track, List<Track>) -> Unit,
    onOpenSearch: () -> Unit,
    vm: HomeViewModel = hiltViewModel(),
    playerVm: PlayerViewModel = hiltViewModel(),
) {
    val colors = VibeWaveTheme.colors
    val recent by vm.recent.collectAsStateWithLifecycle()
    val trending by vm.trending.collectAsStateWithLifecycle()
    val nowPlaying by playerVm.currentTrack.collectAsStateWithLifecycle()

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = topInset + 8.dp,
            bottom = 180.dp,
        ),
    ) {
        // ── Greeting header ──────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Wavee",
                    color = colors.onSurface,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .clickable(onClick = onOpenSearch),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = colors.onSurface,
                    )
                }
            }
        }

        // ── Recently played strip ────────────────────────────────────────
        if (recent.isNotEmpty()) {
            item {
                Text(
                    stringResource(R.string.home_recently_played),
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(recent.take(15), key = { it.id }) { track ->
                        RecentTile(track = track, onClick = { onTrackClick(track, recent) })
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }

        // ── Trending ─────────────────────────────────────────────────────
        item {
            Text(
                "Сейчас популярно:",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            )
        }

        when (val s = trending) {
            is TrendingState.Loading -> items(6) { ShimmerTrackRow() }
            is TrendingState.Error -> item {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(s.message, color = colors.onSurfaceMuted)
                }
            }
            is TrendingState.Loaded -> items(s.tracks, key = { it.id }) { track ->
                TrackRow(
                    track = track,
                    isPlaying = nowPlaying?.id == track.id,
                    onClick = { onTrackClick(track, s.tracks) },
                )
            }
        }
    }
}

/**
 * A square tile used in the horizontal "Recently played" strip.
 * 140 dp wide, album art on top, title + artist below.
 */
@Composable
private fun RecentTile(track: Track, onClick: () -> Unit) {
    val colors = VibeWaveTheme.colors
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surfaceElevated),
            contentAlignment = Alignment.Center,
        ) {
            if (track.albumArt != null) {
                AsyncImage(
                    model = track.albumArt,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(128.dp),
                )
            } else {
                Icon(Icons.Rounded.MusicNote, null, tint = colors.onSurfaceMuted)
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            track.title,
            color = colors.onSurface,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            track.artist,
            color = colors.onSurfaceMuted,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
