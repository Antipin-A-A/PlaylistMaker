package com.example.playlistmaker.player.ui.state

sealed class TimeTrack(val time: String) {
    class DefauliTime: TimeTrack("00:00")
    class CurentTime(time: String) : TimeTrack(time)
}
