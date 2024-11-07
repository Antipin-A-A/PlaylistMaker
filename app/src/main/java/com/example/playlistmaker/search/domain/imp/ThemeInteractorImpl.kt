package com.example.playlistmaker.search.domain.imp

import com.example.playlistmaker.search.domain.api.interactor.ThemeInteractor
import com.example.playlistmaker.search.domain.api.reposirory.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun switchIsChecked(isChecked: Boolean) {
        repository.switchIsChecked(isChecked)
    }

    override fun getSwitchStatus(): Boolean {
        return repository.getSwitchStatus()
    }
}