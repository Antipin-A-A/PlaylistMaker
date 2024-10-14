package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.interactor.ThemeInteractor
import com.example.playlistmaker.domain.api.reposirory.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun switchIsChecked(isChecked: Boolean) {
        repository.switchIsChecked(isChecked)
    }

    override fun getSwitchStatus(): Boolean {
        return repository.getSwitchStatus()
    }
}