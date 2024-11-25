package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.di.mediaModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingModule
import com.example.playlistmaker.di.sharingModule
import com.example.playlistmaker.settings.domain.api.interact.ThemeInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val themeInteractor: ThemeInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(searchModule, playerModule, settingModule, sharingModule, mediaModule)
        }

        AppTheme.switchTheme(themeInteractor.getSwitchStatus())

    }


}