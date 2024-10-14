package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.Themes.ThemeRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitManager
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.shared_preference.SharedManager
import com.example.playlistmaker.data.shared_preference.TrackStorageRepositoryImpl
import com.example.playlistmaker.domain.api.interactor.ThemeInteractor
import com.example.playlistmaker.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.domain.api.reposirory.ThemeRepository
import com.example.playlistmaker.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var appContext: Context
    fun setContext(context: Context) {
        appContext = context.applicationContext
    }

    fun provideTrackInteractor(): TrackIteractor {
        return TracksInteractorImpl(getTrackRepository(), getTrackStorageRepository())
    }

    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository())
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(RetrofitManager()))
    }

    private fun getTrackStorageRepository(): TrackStorageRepository {
        return TrackStorageRepositoryImpl(SharedManager(appContext))
    }

    private fun getThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(SharedManager(appContext))
    }

}
