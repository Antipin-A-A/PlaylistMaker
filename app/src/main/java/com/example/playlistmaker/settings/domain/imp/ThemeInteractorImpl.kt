package com.example.playlistmaker.settings.domain.imp

import com.example.playlistmaker.settings.domain.api.interact.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.repository.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun switchIsChecked(isChecked: Boolean) {
        repository.switchIsChecked(isChecked)
    }

    override fun getSwitchStatus(): Boolean {
      return  repository.getSwitchStatus()
    }
}