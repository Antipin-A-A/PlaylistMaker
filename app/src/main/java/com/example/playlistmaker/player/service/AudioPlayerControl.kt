package com.example.playlistmaker.player.service

import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.state.TimeTrack
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun getPlayerTime(): StateFlow<TimeTrack>
    fun startPlayer()
    fun pausePlayer()
    fun provideNotificator()
    fun stopNotification()
}