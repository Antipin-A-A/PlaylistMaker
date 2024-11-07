package com.example.playlistmaker.player.domain.api.repository

interface MediaPlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun release()
    fun setOnCompletionListener(function: () -> Unit)
    fun currentPosition(): Long
}