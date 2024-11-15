package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.interact.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.example.playlistmaker.settings.domain.imp.ThemeInteractorImpl
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingModule = module {

    single<ThemeRepository> { ThemeRepositoryImpl(get()) }

    single<ThemeInteractor> { ThemeInteractorImpl(get()) }

    viewModel { SettingsViewModel(get(),get()) }

}
