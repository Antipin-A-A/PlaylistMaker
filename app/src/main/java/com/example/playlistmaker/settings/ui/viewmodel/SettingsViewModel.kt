package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.interact.ThemeInteractor
import com.example.playlistmaker.sharing.domain.api.interact.SharingInteractor

class SettingsViewModel(
    private val providerSwitchStatus: ThemeInteractor,
    private val providerSharing: SharingInteractor
) : ViewModel() {

    private val stateMutable = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = stateMutable

    init {
        getSwitchStatus()
    }

   private fun getSwitchStatus() {
       stateMutable.value = providerSwitchStatus.getSwitchStatus()
    }

    fun switchTheme(isChecked: Boolean) {
        providerSwitchStatus.switchIsChecked(isChecked)
        stateMutable.value = isChecked
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