package com.vibewave.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.vibewave.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vibewave.domain.model.Track
import com.vibewave.ui.components.ShimmerTrackRow
import com.vibewave.ui.components.TrackRow
import com.vibewave.ui.screens.player.PlayerViewModel
import com.vibewave.ui.theme.VibeWaveTheme

/**
 * Search screen — mirrors the third uploaded screenshot.
 *
 * Layout: pill search bar, horizontal genre chips, section title,
 * list of track rows. Content fades between Loading/Loaded/Error.
 */
@Composable
fun SearchScreen(
    onTrackClick: (Track, List<Track>) -> Unit,
    onBack: () -> Unit,
    vm: SearchViewModel = hiltViewModel(),
    playerVm: PlayerViewModel = hiltViewModel(),
) {
    val colors = VibeWaveTheme.colors
    val query by vm.query.collectAsStateWithLifecycle()
    val state by vm.state.collectAsStateWithLifecycle()
    val sectionTitle by vm.sectionTitle.collectAsStateWithLifecycle()
    val nowPlaying by playerVm.currentTrack.collectAsStateWithLifecycle()

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topInset),
    ) {
        // ── Pill search bar ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(colors.surface)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Rounded.Search,
                contentDescription = null,
                tint = colors.onSurfaceMuted,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.size(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        "Введите запрос",
                        color = colors.onSurfaceMuted,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = vm::onQueryChanged,
                    singleLine = true,
                    cursorBrush = SolidColor(colors.accent),
                    textStyle = TextStyle(
                        color = colors.onSurface,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (query.isNotEmpty()) {
                IconButton(onClick = vm::clear, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Rounded.Close, stringResource(R.string.search_clear), tint = colors.onSurfaceMuted)
                }
            }
        }

        // ── Genre chips ──────────────────────────────────────────────────
        GenreChipRow { vm.onQueryChanged(it) }

        // ── Section title ────────────────────────────────────────────────
        Text(
            stringResource(sectionTitle),
            color = colors.onSurface,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        // ── Content ──────────────────────────────────────────────────────
        AnimatedContent(
            targetState = state,
            transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
            label = "searchContent",
            modifier = Modifier.fillMaxSize(),
        ) { s ->
            when (s) {
                is SearchUiState.Loading -> ShimmerList()
                is SearchUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(s.message, color = colors.onSurfaceMuted)
                }
                is SearchUiState.Loaded -> LazyColumn(
                    contentPadding = PaddingValues(bottom = 180.dp),
                ) {
                    items(s.tracks, key = { it.id }) { track ->
                        TrackRow(
                            track = track,
                            isPlaying = nowPlaying?.id == track.id,
                            onClick = { onTrackClick(track, s.tracks) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GenreChipRow(onGenre: (String) -> Unit) {
    val colors = VibeWaveTheme.colors
    val scroll = rememberScrollState()
    // Pair = (display label resource, search query sent to Deezer API).
    // Query stays in English so the API returns relevant results regardless
    // of the UI language.
    val genres = listOf(
        R.string.genre_pop to "поп",
        R.string.genre_rock to "Рок",
        R.string.genre_hiphop to "Хип-хоп",
        R.string.genre_indie to "Инди",
        R.string.genre_rnb to "r&b",
        R.string.genre_electro to "Электро",
        R.string.genre_jazz to "Джаз",
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scroll)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        genres.forEach { (labelRes, query) ->
            Text(
                text = stringResource(labelRes),
                color = colors.onSurface,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surface)
                    .clickable { onGenre(query) }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            )
        }
    }
}

@Composable
private fun ShimmerList() {
    Column(Modifier.fillMaxSize()) {
        repeat(6) { ShimmerTrackRow() }
    }
}
