package com.example.playlistmaker.data.Themes

import com.example.playlistmaker.data.shared_preference.SharedManager
import com.example.playlistmaker.domain.api.reposirory.ThemeRepository
import com.example.playlistmaker.presentation.theme.AppTheme

class ThemeRepositoryImpl(private val sharedManager: SharedManager) :
    ThemeRepository {

    override fun switchIsChecked(isChecked: Boolean) {
        AppTheme.switchTheme(isChecked)
        sharedManager.putSwitchStatus(isChecked)
    }

    override fun getSwitchStatus(): Boolean {
        return sharedManager.getSwitchStatus()
    }
}
