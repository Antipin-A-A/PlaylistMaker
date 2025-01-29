package com.example.playlistmaker.player.ui.viewmodel

import com.example.playlistmaker.playlist.domain.model.PlayList

sealed interface PlayListStateForMusic {
    data object Loading : PlayListStateForMusic

    data class Content(
        val playlist: List<PlayList>
    ) : PlayListStateForMusic
}