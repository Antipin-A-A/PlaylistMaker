package com.example.playlistmaker.settings.domain.api.repository

interface ThemeRepository {
    fun switchIsChecked(isChecked: Boolean)
    fun getSwitchStatus(): Boolean
}