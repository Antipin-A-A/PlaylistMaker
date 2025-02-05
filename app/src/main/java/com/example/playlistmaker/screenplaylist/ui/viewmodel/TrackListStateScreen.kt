package com.example.playlistmaker.screenplaylist.ui.viewmodel

import com.example.playlistmaker.search.domain.modeles.Track

sealed interface TrackListStateScreen {

    data object Loading : TrackListStateScreen
    data class Content(
        val track: List<Track>
    ) : TrackListStateScreen

    data object Empty : TrackListStateScreen

}