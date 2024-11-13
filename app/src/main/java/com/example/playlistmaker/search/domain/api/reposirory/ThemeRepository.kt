package com.example.playlistmaker.search.domain.api.reposirory

interface ThemeRepository {
    fun switchIsChecked(isChecked: Boolean)
    fun getSwitchStatus(): Boolean
}