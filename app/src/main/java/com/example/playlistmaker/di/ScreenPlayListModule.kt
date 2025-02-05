package com.example.playlistmaker.di

import com.example.playlistmaker.screenplaylist.ui.viewmodel.ScreenPlaylistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenPlayListModule = module {

    viewModel {
        ScreenPlaylistViewModel(get(), get(), get())
    }
}