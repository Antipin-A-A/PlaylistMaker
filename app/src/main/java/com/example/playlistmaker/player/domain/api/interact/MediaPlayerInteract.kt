package com.example.playlistmaker.player.domain.api.interact

import com.example.playlistmaker.player.ui.state.PlayerState

interface MediaPlayerInteract {
    val playerState: PlayerState
    fun preparePlayer(url: String)
    fun play()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun setOnCompletionListener(function: () -> Unit)
    fun currentPosition(): Long
    fun isPlaying():Boolean
}