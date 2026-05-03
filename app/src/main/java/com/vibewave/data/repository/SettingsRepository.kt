package com.vibewave.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vibewave.data.datastore.AccentColor
import com.vibewave.data.datastore.AppFont
import com.vibewave.data.datastore.PlayerStyle
import com.vibewave.data.datastore.ThemePalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for user appearance preferences.
 *
 * Exposes a cold [Flow] of [AppearanceSettings]; each screen that cares
 * about theming simply collects it. All writes suspend so they never
 * block the UI thread.
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    companion object Keys {
        val PALETTE = stringPreferencesKey("palette")
        val ACCENT = stringPreferencesKey("accent")
        val FONT = stringPreferencesKey("font")
        val PLAYER_STYLE = stringPreferencesKey("player_style")
        val DYNAMIC_FROM_ART = stringPreferencesKey("dynamic_from_art")
        val LANGUAGE = stringPreferencesKey("language")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
    }

    val settings: Flow<AppearanceSettings> = dataStore.data.map { prefs ->
        AppearanceSettings(
            palette = prefs[PALETTE]?.let { runCatching { ThemePalette.valueOf(it) }.getOrNull() }
                ?: ThemePalette.OBSIDIAN,
            accent = prefs[ACCENT]?.let { runCatching { AccentColor.valueOf(it) }.getOrNull() }
                ?: AccentColor.GREEN,
            font = prefs[FONT]?.let { runCatching { AppFont.valueOf(it) }.getOrNull() }
                ?: AppFont.TT_HOVES,
            playerStyle = prefs[PLAYER_STYLE]?.let { runCatching { PlayerStyle.valueOf(it) }.getOrNull() }
                ?: PlayerStyle.CLASSIC,
            dynamicFromArt = prefs[DYNAMIC_FROM_ART]?.toBooleanStrictOrNull() ?: true,
            language = prefs[LANGUAGE] ?: "ru",
            avatarUri = prefs[AVATAR_URI],
        )
    }

    suspend fun setPalette(p: ThemePalette) = dataStore.edit { it[PALETTE] = p.name }
    suspend fun setAccent(a: AccentColor) = dataStore.edit { it[ACCENT] = a.name }
    suspend fun setFont(f: AppFont) = dataStore.edit { it[FONT] = f.name }
    suspend fun setPlayerStyle(s: PlayerStyle) = dataStore.edit { it[PLAYER_STYLE] = s.name }
    suspend fun setDynamicFromArt(v: Boolean) = dataStore.edit { it[DYNAMIC_FROM_ART] = v.toString() }
    suspend fun setLanguage(lang: String) = dataStore.edit { it[LANGUAGE] = lang }
    suspend fun setAvatarUri(uri: String?) = dataStore.edit {
        if (uri != null) it[AVATAR_URI] = uri else it.remove(AVATAR_URI)
    }
}

data class AppearanceSettings(
    val palette: ThemePalette,
    val accent: AccentColor,
    val font: AppFont,
    val playerStyle: PlayerStyle,
    val dynamicFromArt: Boolean,
    val language: String = "ru",
    val avatarUri: String? = null,
)
