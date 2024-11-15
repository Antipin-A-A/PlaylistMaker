package com.example.playlistmaker.settings.domain.api.interact

interface ThemeInteractor {
    fun switchIsChecked(isChecked: Boolean)
    fun getSwitchStatus(): Boolean
}