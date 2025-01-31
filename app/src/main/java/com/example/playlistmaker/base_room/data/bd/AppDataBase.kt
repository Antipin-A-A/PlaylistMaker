package com.example.playlistmaker.base_room.data.bd

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.base_room.data.convector.Converters

@Database(
    version = 1,
    entities = [TrackEntity::class, PlayListEntity::class, AllTracksEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playListDao(): PlayListDao
    abstract fun allTracksDao(): AllTracksDao
}