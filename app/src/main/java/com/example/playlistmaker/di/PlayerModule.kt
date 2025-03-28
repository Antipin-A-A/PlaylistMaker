package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.interact.MediaPlayerInteract
import com.example.playlistmaker.player.domain.api.repository.MediaPlayerRepository
import com.example.playlistmaker.player.domain.imp.MediaPlayerInteractImpl
import com.example.playlistmaker.player.ui.viewmodel.MusicFragmentViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    factory{MediaPlayer()}
    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }
    factory<MediaPlayerInteract> {
        MediaPlayerInteractImpl(get())
    }
    viewModel { MusicFragmentViewModel(get(),get(), get()) }

}