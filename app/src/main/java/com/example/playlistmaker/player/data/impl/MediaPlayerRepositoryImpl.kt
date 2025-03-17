package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.repository.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun release() {
        playerState = STATE_DEFAULT
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(function: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            function()
        }
    }

    override fun currentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    private companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT
}