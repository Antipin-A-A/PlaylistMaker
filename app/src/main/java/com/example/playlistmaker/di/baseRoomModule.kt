package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.base_room.data.bd.AppDataBase
import com.example.playlistmaker.base_room.data.convector.TrackDbConvertor
import com.example.playlistmaker.base_room.data.network.RoomRepositoryImpl
import com.example.playlistmaker.base_room.domain.api.RoomInteract
import com.example.playlistmaker.base_room.domain.api.RoomRepository
import com.example.playlistmaker.base_room.domain.imp.RoomInteractImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val baseRoomModule = module {
    factory {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .build()
    }

    factory<RoomRepository> {
        RoomRepositoryImpl(get(), get())
    }

    factory <RoomInteract> {
        RoomInteractImpl(get())
    }

    factory { TrackDbConvertor() }
}