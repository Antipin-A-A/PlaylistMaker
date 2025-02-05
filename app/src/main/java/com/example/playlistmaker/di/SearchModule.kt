package com.example.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.settings.data.ThemeRepositoryImpl
import com.example.playlistmaker.search.data.network.ConnectedManager
import com.example.playlistmaker.search.data.network.RetrofitManager
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackRepositoryImpl
import com.example.playlistmaker.search.data.shared_preference.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.playlistmaker.search.data.shared_preference.SharedManager
import com.example.playlistmaker.search.data.shared_preference.TrackStorageRepositoryImpl
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.settings.domain.api.repository.ThemeRepository
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.search.domain.imp.TracksInteractorImpl
import com.example.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

    val searchModule = module {

        factory { SharedManager(get()) }

        single {
            androidContext().getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        }

        single<NetworkClient> {
            RetrofitNetworkClient(get(), get())
        }
        single { RetrofitManager() }
        single { ConnectedManager(androidContext()) }

        single<ThemeRepository> {
            ThemeRepositoryImpl(get())
        }

        single<TrackRepository> {
            TrackRepositoryImpl(get())
        }

        single<TrackStorageRepository> {
            TrackStorageRepositoryImpl(get(), get())
        }

        single<TrackIteractor> {
            TracksInteractorImpl(get(),get())
        }

        viewModel {
            SearchActivityViewModel(get())
        }

    }

