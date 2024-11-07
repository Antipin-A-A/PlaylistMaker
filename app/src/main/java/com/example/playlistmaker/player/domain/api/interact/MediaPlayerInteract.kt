package com.example.playlistmaker.player.domain.api.interact

interface MediaPlayerInteract {
    fun preparePlayer(url: String)
    fun play()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun setOnCompletionListener(function: () -> Unit)
    fun currentPosition(): Long
}