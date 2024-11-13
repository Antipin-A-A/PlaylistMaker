package com.example.playlistmaker.search.data.Themes

import com.example.playlistmaker.search.data.shared_preference.SharedManager
import com.example.playlistmaker.search.domain.api.reposirory.ThemeRepository
import com.example.playlistmaker.search.util.AppTheme

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
