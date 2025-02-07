package com.example.playlistmaker.playlist.ui.viewmodel

import com.example.playlistmaker.playlist.domain.model.PlayList

sealed interface PlayListState {
    data object Loading : PlayListState
    data class Content(val playlist: List<PlayList>) : PlayListState
    data class Empty(val message: String) : PlayListState
}