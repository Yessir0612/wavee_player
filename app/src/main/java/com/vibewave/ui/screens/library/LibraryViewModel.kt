package com.vibewave.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.data.repository.LocalMediaRepository
import com.vibewave.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI states for the Library screen:
 *
 *  • [Idle]           — permission not yet requested
 *  • [Loading]        — scanning MediaStore
 *  • [Ready]          — list of tracks (may be empty)
 *  • [PermissionDenied] — user refused permission
 */
sealed class LibraryUiState {
    data object Idle : LibraryUiState()
    data object Loading : LibraryUiState()
    data class Ready(val tracks: List<Track>) : LibraryUiState()
    data object PermissionDenied : LibraryUiState()
}

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val localMediaRepo: LocalMediaRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<LibraryUiState>(LibraryUiState.Idle)
    val state: StateFlow<LibraryUiState> = _state.asStateFlow()

    /** Called once permission is granted. */
    fun onPermissionGranted() {
        if (_state.value is LibraryUiState.Loading) return
        _state.value = LibraryUiState.Loading
        viewModelScope.launch {
            val tracks = runCatching { localMediaRepo.loadAll() }.getOrDefault(emptyList())
            _state.value = LibraryUiState.Ready(tracks)
        }
    }

    fun onPermissionDenied() {
        _state.value = LibraryUiState.PermissionDenied
    }

    fun refresh() = onPermissionGranted()
}
