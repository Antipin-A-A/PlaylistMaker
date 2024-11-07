package com.example.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.ui.state.TrackListState

class SearchActivityViewModel : ViewModel() {

    private val trackIteractor by lazy { Creator.provideTrackInteractor() }

    private val stateMutable = MutableLiveData<TrackListState>()
    val state: LiveData<TrackListState> = stateMutable

    init {
        getHistoryTrackList()
    }

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null

    fun saveHistoryTrack(track: Track) {
        trackIteractor.saveTrackList(track)
    }

    fun saveTrack(track: Track) {
        trackIteractor.saveTrack(track)
    }

    fun getHistoryTrackList() {
        renderState(TrackListState.getHistoryList(track = trackIteractor.loadTracksList()))
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
                is TrackListState.getHistoryList -> trackState
            }

        }
    }

    fun observeMediaState(): LiveData<TrackListState> = mediatorStateLiveData


    private fun iTunesServiceSearch(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(TrackListState.Loading)
            trackIteractor.searchTrack(searchText,
                object : TrackIteractor.TrackConsumer {
                    override fun consume(foundTreks: List<Track>?, errorMessage: String?) {
                        val trackSearch = mutableListOf<Track>()
                        if (foundTreks != null) {
                            trackSearch.addAll(foundTreks)
                        }
                        if (errorMessage != null) {
                            renderState(TrackListState.Error(errorMessage))
                        } else if (trackSearch.isEmpty() && searchText.isNotEmpty()) {
                            renderState(TrackListState.Empty(errorMessage.toString()))
                        } else {
                            renderState(
                                TrackListState.Content(
                                    track = trackSearch
                                )
                            )

                        }

                    }
                })
        } else {
            getHistoryTrackList()
        }
    }


    private fun renderState(state: TrackListState) {
        stateMutable.postValue(state)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        val searchRunnable = Runnable { iTunesServiceSearch(changedText) }
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun searchViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchActivityViewModel()
            }
        }
    }
}