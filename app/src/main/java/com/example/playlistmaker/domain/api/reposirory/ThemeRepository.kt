package com.example.playlistmaker.domain.api.reposirory

interface ThemeRepository {
    fun switchIsChecked(isChecked: Boolean)
    fun getSwitchStatus(): Boolean
}