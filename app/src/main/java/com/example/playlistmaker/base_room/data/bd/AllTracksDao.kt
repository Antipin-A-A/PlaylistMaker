package com.example.playlistmaker.base_room.data.bd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AllTracksDao {
    @Insert(entity = AllTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInTableAllTracks(track: AllTracksEntity)

    @Query("SELECT * FROM all_tracks_table")
    suspend fun getTracks(): List<AllTracksEntity>

}