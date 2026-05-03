package com.vibewave.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.data.datastore.AccentColor
import com.vibewave.data.datastore.AppFont
import com.vibewave.data.datastore.PlayerStyle
import com.vibewave.data.datastore.ThemePalette
import com.vibewave.data.repository.AppearanceSettings
import com.vibewave.data.repository.AuthRepository
import com.vibewave.data.repository.AuthUser
import com.vibewave.data.repository.FavoritesRepository
import com.vibewave.data.repository.HistoryRepository
import com.vibewave.data.repository.SettingsRepository
import com.vibewave.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val settingsRepo: SettingsRepository,
    private val historyRepo: HistoryRepository,
    private val favoritesRepo: FavoritesRepository,
    private val authRepo: AuthRepository,
) : ViewModel() {

    val settings: StateFlow<AppearanceSettings?> = settingsRepo.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val user: StateFlow<AuthUser?> = authRepo.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val recent: StateFlow<List<Track>> = historyRepo.recent
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val top: StateFlow<List<Track>> = historyRepo.top
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val favorites: StateFlow<List<Track>> = favoritesRepo.all
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalCount: StateFlow<Int> = historyRepo.totalCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val favoritesCount: StateFlow<Int> = favoritesRepo.count
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun setPalette(p: ThemePalette) = viewModelScope.launch { settingsRepo.setPalette(p) }
    fun setAccent(a: AccentColor) = viewModelScope.launch { settingsRepo.setAccent(a) }
    fun setFont(f: AppFont) = viewModelScope.launch { settingsRepo.setFont(f) }
    fun setPlayerStyle(s: PlayerStyle) = viewModelScope.launch { settingsRepo.setPlayerStyle(s) }
    fun setDynamicFromArt(v: Boolean) = viewModelScope.launch { settingsRepo.setDynamicFromArt(v) }
    fun setLanguage(lang: String) = viewModelScope.launch { settingsRepo.setLanguage(lang) }
    fun setAvatarUri(uri: String?) = viewModelScope.launch { settingsRepo.setAvatarUri(uri) }
    fun clearHistory() = viewModelScope.launch { historyRepo.clearAll() }
    fun removeFavorite(trackId: Long) = viewModelScope.launch { favoritesRepo.remove(trackId) }

    /** Sign out; root Crossfade in VibeWaveApp picks this up and swaps to AuthFlow. */
    fun signOut() = authRepo.signOut()
}
