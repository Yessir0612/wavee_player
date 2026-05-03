package com.vibewave.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.core.result.Outcome
import com.vibewave.data.repository.HistoryRepository
import com.vibewave.data.repository.MusicRepository
import com.vibewave.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Home = "recently played" strip at the top + trending tracks below.
 *
 * Recently played comes from the Room-backed history flow, so it updates
 * live whenever the user plays anything — no refresh required.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    musicRepo: MusicRepository,
    historyRepo: HistoryRepository,
) : ViewModel() {

    val recent: StateFlow<List<Track>> = historyRepo.recent
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _trending = MutableStateFlow<TrendingState>(TrendingState.Loading)
    val trending: StateFlow<TrendingState> = _trending.asStateFlow()

    init {
        viewModelScope.launch {
            _trending.value = when (val res = musicRepo.chart()) {
                is Outcome.Success -> TrendingState.Loaded(res.data)
                is Outcome.Error -> TrendingState.Error(res.message)
                Outcome.Loading -> TrendingState.Loading
            }
        }
    }
}

sealed interface TrendingState {
    data object Loading : TrendingState
    data class Loaded(val tracks: List<Track>) : TrendingState
    data class Error(val message: String) : TrendingState
}
