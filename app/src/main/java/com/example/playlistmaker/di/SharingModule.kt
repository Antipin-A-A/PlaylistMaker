package com.example.playlistmaker.di

import com.example.playlistmaker.sharing.data.imp.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.imp.SharingRepositoryImp
import com.example.playlistmaker.sharing.domain.api.interact.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.interact.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.reposytory.SharingRepository
import com.example.playlistmaker.sharing.domain.imp.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val sharingModule = module {

    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }

    single<SharingRepository> { SharingRepositoryImp(androidContext()) }

    factory<SharingInteractor> { SharingInteractorImpl(get(), get()) }

}