package com.example.playlistmaker.settings.data

import com.example.playlistmaker.search.data.shared_preference.SharedManager
import com.example.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.example.playlistmaker.app.AppTheme

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
