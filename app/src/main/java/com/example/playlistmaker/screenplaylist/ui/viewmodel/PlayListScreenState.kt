package com.example.playlistmaker.screenplaylist.ui.viewmodel

import com.example.playlistmaker.playlist.domain.model.PlayList

sealed  class PlayListScreenState {
    data object Loading : PlayListScreenState()
    data class Content(val playlist: PlayList) : PlayListScreenState()

}