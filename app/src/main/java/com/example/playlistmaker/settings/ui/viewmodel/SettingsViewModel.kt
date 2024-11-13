package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.search.domain.api.interactor.ThemeInteractor
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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    providerTheme = Creator.provideThemeInteractor(),
                    providerSharing = Creator.provideSharingInteractor()
                )
            }
        }
    }
}