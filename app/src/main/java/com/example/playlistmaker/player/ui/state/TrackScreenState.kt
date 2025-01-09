package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.search.domain.modeles.Track

sealed class TrackScreenState {
    data object Loading : TrackScreenState()
    data object Complete : TrackScreenState()
    data class Content(
        val trackModel: Track?
    ) : TrackScreenState()

    data class TimeTrack(val time: String) : TrackScreenState()
}
