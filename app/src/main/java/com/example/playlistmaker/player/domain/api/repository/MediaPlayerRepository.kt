package com.example.playlistmaker.player.domain.api.repository

import com.example.playlistmaker.player.ui.state.PlayerState

interface MediaPlayerRepository {
    val playerState: PlayerState
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun setOnCompletionListener(function: () -> Unit)
    fun currentPosition(): Long
    fun isPlaying(): Boolean
}