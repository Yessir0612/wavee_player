package com.vibewave.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.vibewave.R
import com.vibewave.data.datastore.AccentColor
import com.vibewave.data.datastore.AppFont
import com.vibewave.data.datastore.PlayerStyle
import com.vibewave.data.datastore.ThemePalette
import com.vibewave.data.repository.AppearanceSettings
import com.vibewave.domain.model.Track
import com.vibewave.ui.components.TrackRow
import com.vibewave.ui.screens.player.PlayerViewModel
import com.vibewave.ui.theme.PaletteColors
import com.vibewave.ui.theme.VibeWaveTheme
import java.io.File

/**
 * Profile: play stats, customization pickers, full history list.
 *
 * Customization is live — any tap persists through DataStore and the root
 * theme flow, so the whole app restyles instantly.
 */
@Composable
fun ProfileScreen(
    onTrackClick: (Track, List<Track>) -> Unit,
    vm: ProfileViewModel = hiltViewModel(),
    playerVm: PlayerViewModel = hiltViewModel(),
) {
    val colors = VibeWaveTheme.colors
    val settings by vm.settings.collectAsStateWithLifecycle()
    val user by vm.user.collectAsStateWithLifecycle()
    val recent by vm.recent.collectAsStateWithLifecycle()
    val top by vm.top.collectAsStateWithLifecycle()
    val favorites by vm.favorites.collectAsStateWithLifecycle()
    val totalCount by vm.totalCount.collectAsStateWithLifecycle()
    val favoritesCount by vm.favoritesCount.collectAsStateWithLifecycle()
    val nowPlaying by playerVm.currentTrack.collectAsStateWithLifecycle()

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val s = settings ?: return

    // ── Avatar picker setup ───────────────────────────────────────────────────
    val context = LocalContext.current
    var showAvatarDialog by remember { mutableStateOf(false) }

    // Temporary file for camera capture
    val cameraFile = remember {
        File(context.cacheDir, "avatar_capture.jpg").also { it.parentFile?.mkdirs() }
    }
    val cameraUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", cameraFile)
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Persist access so we can read it later even after app restart
            context.contentResolver.takePersistableUriPermission(
                uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            vm.setAvatarUri(uri.toString())
        }
    }

    // Camera capture
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) vm.setAvatarUri(cameraUri.toString())
    }

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text(stringResource(R.string.profile_avatar_choose)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.profile_avatar_gallery),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.surfaceElevated)
                            .clickable {
                                showAvatarDialog = false
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .padding(16.dp),
                        color = colors.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = stringResource(R.string.profile_avatar_camera),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.surfaceElevated)
                            .clickable {
                                showAvatarDialog = false
                                cameraLauncher.launch(cameraUri)
                            }
                            .padding(16.dp),
                        color = colors.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (s.avatarUri != null) {
                        Text(
                            text = stringResource(R.string.profile_avatar_remove),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x22FF4444))
                                .clickable {
                                    showAvatarDialog = false
                                    vm.setAvatarUri(null)
                                }
                                .padding(16.dp),
                            color = Color(0xFFFF6B6B),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text(stringResource(R.string.action_back), color = colors.onSurfaceMuted)
                }
            },
            containerColor = colors.surface,
            titleContentColor = colors.onSurface,
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(
            top = topInset + 24.dp,
            bottom = 180.dp,
        ),
    ) {
        // ── Avatar + name ────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Avatar circle — tap to pick
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(colors.accent.copy(alpha = 0.25f))
                        .clickable { showAvatarDialog = true },
                    contentAlignment = Alignment.Center,
                ) {
                    if (s.avatarUri != null) {
                        AsyncImage(
                            model = Uri.parse(s.avatarUri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(96.dp).clip(CircleShape),
                        )
                    } else {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier.size(48.dp),
                        )
                    }
                    // Small camera badge in bottom-right corner
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(colors.accent),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Rounded.AddAPhoto,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = user?.displayName?.takeIf { it.isNotBlank() } ?: stringResource(R.string.profile_default_user),
                    color = colors.onSurface,
                    style = MaterialTheme.typography.headlineLarge,
                )
                if (user?.email != null) {
                    Text(
                        user!!.email!!,
                        color = colors.onSurfaceMuted,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    "$totalCount played · $favoritesCount liked",
                    color = colors.onSurfaceMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        // ── Top played strip ─────────────────────────────────────────────
        if (top.isNotEmpty()) {
            item {
                SectionHeader(stringResource(R.string.profile_top_tracks))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(top.take(10), key = { it.id }) { track ->
                        TopTile(track = track, onClick = { onTrackClick(track, top) })
                    }
                }
            }
        }

        // ── Liked tracks ─────────────────────────────────────────────────
        if (favorites.isNotEmpty()) {
            item { SectionHeader("Liked ❤") }
            items(favorites.take(20), key = { "fav-${it.id}" }) { track ->
                TrackRow(
                    track = track,
                    isPlaying = nowPlaying?.id == track.id,
                    onClick = { onTrackClick(track, favorites) },
                )
            }
        }

        // ── APPEARANCE ───────────────────────────────────────────────────
        item { SectionHeader(stringResource(R.string.profile_appearance)) }

        item { SubsectionHeader(stringResource(R.string.profile_theme_palette)) }
        item { PalettePicker(current = s.palette, onSelect = vm::setPalette) }

        item { SubsectionHeader(stringResource(R.string.profile_accent_color)) }
        item { AccentPicker(current = s.accent, onSelect = vm::setAccent) }

        item { SubsectionHeader(stringResource(R.string.profile_font)) }
        item { FontPicker(current = s.font, onSelect = vm::setFont) }

        item { SubsectionHeader(stringResource(R.string.profile_player_style)) }
        item { PlayerStylePicker(current = s.playerStyle, onSelect = vm::setPlayerStyle) }

        item { DynamicArtToggle(s) { vm.setDynamicFromArt(it) } }

        // ── LANGUAGE ─────────────────────────────────────────────────────
        item { SectionHeader(stringResource(R.string.profile_language)) }
        item { LanguagePicker(current = s.language, onSelect = vm::setLanguage) }

        // ── HISTORY ──────────────────────────────────────────────────────
        item { SectionHeader(stringResource(R.string.profile_history)) }

        if (recent.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        stringResource(R.string.profile_nothing_yet),
                        color = colors.onSurfaceMuted,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        } else {
            items(recent, key = { it.id }) { track ->
                TrackRow(
                    track = track,
                    isPlaying = nowPlaying?.id == track.id,
                    onClick = { onTrackClick(track, recent) },
                )
            }
            item {
                Text(
                    stringResource(R.string.profile_clear_history),
                    color = colors.accent,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { vm.clearHistory() }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                )
            }
        }

        // ── Sign out — always shown at the very bottom ───────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .clickable { vm.signOut() }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    stringResource(R.string.profile_sign_out),
                    color = Color(0xFFFF6B6B),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

// ── Reusable section / subsection headers ────────────────────────────────

@Composable
private fun SectionHeader(text: String) {
    val colors = VibeWaveTheme.colors
    Text(
        text,
        color = colors.onSurface,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 8.dp),
    )
}

@Composable
private fun SubsectionHeader(text: String) {
    val colors = VibeWaveTheme.colors
    Text(
        text,
        color = colors.onSurfaceMuted,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 8.dp),
    )
}

// ── Palette picker: preview bubbles ───────────────────────────────────────

@Composable
private fun PalettePicker(current: ThemePalette, onSelect: (ThemePalette) -> Unit) {
    val colors = VibeWaveTheme.colors
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(ThemePalette.values().toList()) { palette ->
            val selected = palette == current
            val scheme = PaletteColors.schemeFor(palette)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(scheme.background)
                        .border(
                            width = if (selected) 2.dp else 0.dp,
                            color = if (selected) colors.accent else Color.Transparent,
                            shape = CircleShape,
                        )
                        .clickable { onSelect(palette) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(palette.emoji, style = MaterialTheme.typography.headlineMedium)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    palette.displayName,
                    color = if (selected) colors.accent else colors.onSurfaceMuted,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

// ── Accent picker: color swatches ─────────────────────────────────────────

@Composable
private fun AccentPicker(current: AccentColor, onSelect: (AccentColor) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(AccentColor.values().toList()) { accent ->
            val selected = accent == current
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(accent.argb))
                    .border(
                        width = if (selected) 3.dp else 0.dp,
                        color = Color.White,
                        shape = CircleShape,
                    )
                    .clickable { onSelect(accent) },
                contentAlignment = Alignment.Center,
            ) {
                if (selected) {
                    Icon(Icons.Rounded.Check, null, tint = Color.White)
                }
            }
        }
    }
}

// ── Font picker ───────────────────────────────────────────────────────────

@Composable
private fun FontPicker(current: AppFont, onSelect: (AppFont) -> Unit) {
    val colors = VibeWaveTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        AppFont.values().forEach { font ->
            val selected = font == current
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected) colors.accent.copy(alpha = 0.2f) else colors.surface)
                    .border(
                        width = if (selected) 2.dp else 0.dp,
                        color = if (selected) colors.accent else Color.Transparent,
                        shape = RoundedCornerShape(14.dp),
                    )
                    .clickable { onSelect(font) }
                    .padding(horizontal = 14.dp, vertical = 14.dp),
            ) {
                Text(
                    "Aa",
                    color = colors.onSurface,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                )
                Text(font.displayName, color = colors.onSurfaceMuted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// ── Player style picker ───────────────────────────────────────────────────

@Composable
private fun PlayerStylePicker(current: PlayerStyle, onSelect: (PlayerStyle) -> Unit) {
    val colors = VibeWaveTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        PlayerStyle.values().forEach { style ->
            val selected = style == current
            Text(
                style.displayName,
                color = if (selected) colors.accent else colors.onSurface,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) colors.accent.copy(alpha = 0.18f) else colors.surface)
                    .clickable { onSelect(style) }
                    .padding(vertical = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

// ── Dynamic colors from album art toggle ──────────────────────────────────

@Composable
private fun DynamicArtToggle(s: AppearanceSettings, onChange: (Boolean) -> Unit) {
    val colors = VibeWaveTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(R.string.profile_dynamic_colors),
                color = colors.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                stringResource(R.string.profile_dynamic_colors_desc),
                color = colors.onSurfaceMuted,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Switch(
            checked = s.dynamicFromArt,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colors.accent,
            ),
        )
    }
}

// ── Top-tile used in "Your top tracks" ────────────────────────────────────

@Composable
private fun TopTile(track: Track, onClick: () -> Unit) {
    val colors = VibeWaveTheme.colors
    Column(
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surfaceElevated),
        ) {
            if (track.albumArt != null) {
                coil.compose.AsyncImage(
                    model = track.albumArt,
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.size(120.dp),
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            track.title,
            color = colors.onSurface,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
        )
        Text(
            track.artist,
            color = colors.onSurfaceMuted,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
        )
    }
}

// ── Language picker ────────────────────────────────────────────────────────
@Composable
private fun LanguagePicker(
    current: String,
    onSelect: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LangChip(
            label = stringResource(R.string.profile_language_russian),
            selected = current == "ru",
            onClick = { onSelect("ru") },
            modifier = Modifier.weight(1f),
        )
        LangChip(
            label = stringResource(R.string.profile_language_english),
            selected = current == "en",
            onClick = { onSelect("en") },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun LangChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = VibeWaveTheme.colors
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected) colors.accent.copy(alpha = 0.18f)
                else Color.White.copy(alpha = 0.05f)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) colors.accent else colors.onSurface,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}
