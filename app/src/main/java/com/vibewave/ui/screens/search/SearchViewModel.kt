package com.vibewave.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibewave.core.result.Outcome
import com.vibewave.data.repository.MusicRepository
import com.vibewave.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search screen state machine.
 *
 * Empty query → show trending tracks (/chart).
 * Non-empty   → debounced search after 350ms of idle typing.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MusicRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _state = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val _sectionTitle = MutableStateFlow(com.vibewave.R.string.search_trending)
    val sectionTitle: StateFlow<Int> = _sectionTitle.asStateFlow()

    private var job: Job? = null

    init {
        loadTrending()
    }

    fun onQueryChanged(q: String) {
        _query.value = q
        job?.cancel()
        if (q.isBlank()) {
            loadTrending()
            return
        }
        // Simple manual debounce — Flow.debounce requires wiring up a shared
        // flow + stateIn; this is equivalent and easier to read.
        job = viewModelScope.launch {
            kotlinx.coroutines.delay(350)
            _sectionTitle.value = com.vibewave.R.string.search_results
            _state.value = SearchUiState.Loading
            when (val res = repo.search(q)) {
                is Outcome.Success -> _state.value = SearchUiState.Loaded(res.data)
                is Outcome.Error -> _state.value = SearchUiState.Error(res.message)
                Outcome.Loading -> { /* shouldn't happen */ }
            }
        }
    }

    fun clear() {
        _query.value = ""
        loadTrending()
    }

    private fun loadTrending() {
        _sectionTitle.value = com.vibewave.R.string.search_trending
        viewModelScope.launch {
            _state.value = SearchUiState.Loading
            when (val res = repo.chart()) {
                is Outcome.Success -> _state.value = SearchUiState.Loaded(res.data)
                is Outcome.Error -> _state.value = SearchUiState.Error(res.message)
                Outcome.Loading -> { /* shouldn't happen */ }
            }
        }
    }
}

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Loaded(val tracks: List<Track>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}
