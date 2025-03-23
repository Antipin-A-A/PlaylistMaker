package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.repository.MediaPlayerRepository
import com.example.playlistmaker.player.ui.state.PlayerState

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var _playerState: PlayerState = PlayerState.DEFAULT

    //   private var _playerState = MutableStateFlow<PlayerState>(PlayerState.DEFAULT)
    override val playerState = _playerState

    override fun preparePlayer(url: String) {

        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerState = PlayerState.PREPARED
            //  _playerState.value = PlayerState.PREPARED
        }
        mediaPlayer.setOnErrorListener { _, what, extra ->
            _playerState = PlayerState.PREPARED
            //   _playerState.value = PlayerState.PREPARED
            false
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
        _playerState = PlayerState.PLAYING
        //_playerState.value = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        _playerState = PlayerState.PAUSED
        //  _playerState.value = PlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
        _playerState = PlayerState.DEFAULT
        //  _playerState.value = PlayerState.DEFAULT
    }

    override fun currentPosition(): Long = mediaPlayer.currentPosition.toLong()

    override fun playbackControl() {
        when (_playerState) {
            PlayerState.DEFAULT -> {}
            PlayerState.PAUSED -> {
                startPlayer()
            }

            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PREPARED -> {
                startPlayer()
            }
        }
    }

    override fun setOnCompletionListener(function: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            function()
        }
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}