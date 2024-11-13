package com.example.playlistmaker.player.ui.state

sealed interface PlayerState {
    data class PreparePlayer(val url: Unit) : PlayerState
    data class Play(val startPlayer: Unit) : PlayerState
    data class Pause(val pausePlayer: Unit) : PlayerState
    data class Release(val release: Unit) : PlayerState
    class SetOnComplete(val onCompletionListener: Unit) : PlayerState

}
