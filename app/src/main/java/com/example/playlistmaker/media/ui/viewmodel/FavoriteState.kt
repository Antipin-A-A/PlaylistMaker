package com.example.playlistmaker.media.ui.viewmodel

import com.example.playlistmaker.search.domain.modeles.Track

sealed interface FavoriteState {
    data object Loading : FavoriteState

    data class Content(
        val movies: List<Track>
    ) : FavoriteState

    data class Empty(
        val message: String
    ) : FavoriteState
}