package com.example.playlistmaker.search.ui.state

import com.example.playlistmaker.search.domain.modeles.Track

sealed interface TrackListState {
    data object Loading : TrackListState
    data class Content(val track: List<Track>) : TrackListState
    data class Error(val errorMessage: String) : TrackListState
    data object Empty : TrackListState
    data class GetHistoryList(val track: List<Track>) : TrackListState
}