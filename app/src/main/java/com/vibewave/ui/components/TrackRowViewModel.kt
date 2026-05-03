package com.vibewave.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.data.repository.FavoritesRepository
import com.vibewave.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * One ViewModel instance per TrackRow. Scoped by trackId so each row has
 * its own observer without sharing state across the list.
 *
 * Hilt creates it lazily the first time the row enters composition.
 */
@HiltViewModel
class TrackRowViewModel @Inject constructor(
    private val favorites: FavoritesRepository,
) : ViewModel() {

    fun isFavorite(trackId: Long): Flow<Boolean> =
        favorites.observeIsFavorite(trackId)

    fun toggle(track: Track) {
        viewModelScope.launch { favorites.toggle(track) }
    }
}
