package com.example.playlistmaker.domain.api.interactor

interface ThemeInteractor {
    fun switchIsChecked(isChecked: Boolean)
    fun getSwitchStatus(): Boolean
}