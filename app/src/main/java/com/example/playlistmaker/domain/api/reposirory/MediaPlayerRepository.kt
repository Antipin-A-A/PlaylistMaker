package com.example.playlistmaker.domain.api.reposirory

interface MediaPlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
}