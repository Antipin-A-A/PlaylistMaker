package com.example.playlistmaker.Creator

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.interact.MediaPlayerInteract
import com.example.playlistmaker.player.domain.api.repository.MediaPlayerRepository
import com.example.playlistmaker.player.domain.imp.MediaPlayerInteractImpl
import com.example.playlistmaker.search.data.Themes.ThemeRepositoryImpl
import com.example.playlistmaker.search.data.network.ConnectedManager
import com.example.playlistmaker.search.data.network.RetrofitManager
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackRepositoryImpl
import com.example.playlistmaker.search.data.shared_preference.SharedManager
import com.example.playlistmaker.search.data.shared_preference.TrackStorageRepositoryImpl
import com.example.playlistmaker.search.domain.api.interactor.ThemeInteractor
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.api.reposirory.ThemeRepository
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.search.domain.imp.ThemeInteractorImpl
import com.example.playlistmaker.search.domain.imp.TracksInteractorImpl
import com.example.playlistmaker.sharing.data.imp.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.api.interact.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.interact.SharingInteractor
import com.example.playlistmaker.sharing.domain.imp.SharingInteractorImpl

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

    fun provideMediaPlayerInteractor(): MediaPlayerInteract {
        return MediaPlayerInteractImpl(getMediaPlayerRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator(), appContext)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(
                RetrofitManager(), ConnectedManager(
                    appContext
                )
            )
        )
    }

    private fun getTrackStorageRepository(): TrackStorageRepository {
        return TrackStorageRepositoryImpl(SharedManager(appContext))
    }

    private fun getThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(SharedManager(appContext))
    }

    private  fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(appContext)
    }

}
