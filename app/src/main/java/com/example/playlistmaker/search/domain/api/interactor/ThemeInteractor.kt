package com.example.playlistmaker.search.domain.api.interactor

interface ThemeInteractor {
    fun switchIsChecked(isChecked: Boolean)
    fun getSwitchStatus(): Boolean
}