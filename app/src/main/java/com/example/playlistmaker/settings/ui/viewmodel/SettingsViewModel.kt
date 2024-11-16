package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.interact.ThemeInteractor
import com.example.playlistmaker.sharing.domain.api.interact.SharingInteractor

class SettingsViewModel(
    private val providerTheme: ThemeInteractor,
    private val providerSharing: SharingInteractor
) : ViewModel() {


    fun getTheme(): Boolean {
        return providerTheme.getSwitchStatus()
    }

    fun switchTheme(isChecked: Boolean) {
        providerTheme.switchIsChecked(isChecked)
    }

    fun shareAppLink() {
        providerSharing.shareApp()
    }

    fun sendSupport() {
        providerSharing.openSupport()
    }

    fun openTerms() {
        providerSharing.openTerms()
    }

}