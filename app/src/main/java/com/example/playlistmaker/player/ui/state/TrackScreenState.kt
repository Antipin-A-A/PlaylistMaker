package com.example.playlistmaker.player.ui.state

import com.example.playlistmaker.search.domain.modeles.Track

sealed class TrackScreenState {
    object Loading: TrackScreenState()
    data class Content(
        val trackModel: Track?,
    ): TrackScreenState()
}
