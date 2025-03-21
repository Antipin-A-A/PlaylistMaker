package com.example.playlistmaker.player.ui.state


sealed class PlayerState {
    data object DEFAULT : PlayerState()
    data object PREPARED : PlayerState()
    data object PLAYING : PlayerState()
    data object PAUSED : PlayerState()
}
