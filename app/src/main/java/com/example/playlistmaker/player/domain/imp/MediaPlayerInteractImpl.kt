package com.example.playlistmaker.player.domain.imp

import com.example.playlistmaker.player.domain.api.interact.MediaPlayerInteract
import com.example.playlistmaker.player.domain.api.repository.MediaPlayerRepository

class MediaPlayerInteractImpl(private val repository: MediaPlayerRepository): MediaPlayerInteract {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun play() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
       repository.playbackControl()
    }

    override fun release() {
        repository.release()
    }

    override fun setOnCompletionListener(function: () -> Unit) {
       repository.setOnCompletionListener(function)
    }

    override fun currentPosition(): Long {
       return repository.currentPosition()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

}