package com.example.playlistmaker.di

import com.example.playlistmaker.playlist.ui.viewmodel.NewPlayListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playListModule = module {

    viewModel {
        NewPlayListViewModel(get())
    }
}