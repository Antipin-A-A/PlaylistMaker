package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator

class SettingsViewModel: ViewModel() {

    private val providerTheme by lazy { Creator.provideThemeInteractor() }
    private val providerSharing by lazy { Creator.provideSharingInteractor() }

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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel()
            }
        }
    }
}