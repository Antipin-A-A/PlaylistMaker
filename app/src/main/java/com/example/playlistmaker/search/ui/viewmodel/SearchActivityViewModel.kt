package com.example.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.ui.state.TrackListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivityViewModel(
    private val trackIteractor:TrackIteractor
) : ViewModel() {

    private val stateMutable = MutableLiveData<TrackListState>()
    val state: LiveData<TrackListState> = stateMutable

    init {
        getHistoryTrackList()
    }

    private var latestSearchText: String? = null
    private  var searchJob: Job? = null

    fun saveHistoryTrack(track: Track) {
        trackIteractor.saveTrackList(track)
    }

    fun saveTrack(track: Track) {
        trackIteractor.saveTrack(track)
    }

    fun getHistoryTrackList() {
        renderState(TrackListState.GetHistoryList(track = trackIteractor.loadTracksList()))
    }


    fun removeHistoryTrackList() {
        trackIteractor.removeTrackList()
    }

    private val mediatorStateLiveData = MediatorLiveData<TrackListState>().also { liveData ->
        liveData.addSource(stateMutable) { trackState ->
            liveData.value = when (trackState) {
                is TrackListState.Loading -> trackState
                is TrackListState.Content -> trackState
                is TrackListState.Error -> trackState
                is TrackListState.Empty -> trackState
                is TrackListState.GetHistoryList -> trackState
            }

        }
    }

    fun observeMediaState(): LiveData<TrackListState> = mediatorStateLiveData


    fun iTunesServiceSearch(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(TrackListState.Loading)
            viewModelScope.launch {
                trackIteractor
                    .searchTrack(searchText)
                    .collect{pair ->
                        processResult(pair.first, pair.second, searchText)
                    }
            }
        } else {
            getHistoryTrackList()
        }
    }

    private fun processResult(foundTreks: List<Track>?, errorMessage: String?, searchText: String) {
        val trackSearch = mutableListOf<Track>()
        if (foundTreks != null) {
            trackSearch.addAll(foundTreks)
        }
        when{
            errorMessage != null ->{
                renderState(TrackListState.Error(errorMessage))
            }
            trackSearch.isEmpty() && searchText.isNotEmpty() ->{
                renderState(TrackListState.Empty(errorMessage.toString()))
            }
            else -> {
                renderState(TrackListState.Content(trackSearch))
            }
        }
    }


    private fun renderState(state: TrackListState) {
        stateMutable.postValue(state)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            iTunesServiceSearch(changedText)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}