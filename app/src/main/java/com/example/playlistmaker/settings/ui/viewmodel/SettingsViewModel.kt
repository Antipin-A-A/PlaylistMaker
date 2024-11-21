package com.example.playlistmaker.settings.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.interact.ThemeInteractor
import com.example.playlistmaker.sharing.domain.api.interact.SharingInteractor

class SettingsViewModel(
    private val providerSwitchStatus: ThemeInteractor,
    private val providerSharing: SharingInteractor
) : ViewModel() {

   fun getSwitchStatus():Boolean {
       return providerSwitchStatus.getSwitchStatus()
    }

    fun switchTheme(isChecked: Boolean) {
        providerSwitchStatus.switchIsChecked(isChecked)
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