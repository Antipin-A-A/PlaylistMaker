package com.example.playlistmaker.ui.media_player
import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.reposirory.MediaPlayerRepository

class MediaPlayer(private val url: String, ): MediaPlayerRepository {

    var mediaPlayer = MediaPlayer()

    fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
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

  override  fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        /*четыре константы для каждого из состояния mediaPlay*/
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

     private var playerState = STATE_DEFAULT
}