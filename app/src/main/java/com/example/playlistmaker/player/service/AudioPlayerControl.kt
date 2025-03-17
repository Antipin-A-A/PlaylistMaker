package com.example.playlistmaker.player.service

import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.state.TrackScreenState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun setOnCompleted()
    fun time()
}