package com.vibewave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.data.repository.AppearanceSettings
import com.vibewave.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Exposes the user's appearance settings at the root so the theme applies
 * globally and updates the moment the user changes it in Profile.
 */
@HiltViewModel
class AppRootViewModel @Inject constructor(
    settingsRepo: SettingsRepository,
) : ViewModel() {

    val settings: StateFlow<AppearanceSettings?> = settingsRepo.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
