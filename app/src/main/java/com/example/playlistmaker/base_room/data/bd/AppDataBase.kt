package com.example.playlistmaker.base_room.data.bd

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDataBase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}