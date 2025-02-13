package com.example.playlistmaker.base_room.data.bd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AllTracksDao {
    @Insert(entity = AllTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInTableAllTracks(track: AllTracksEntity)

    @Query("SELECT * FROM all_tracks_table")
    suspend fun getTracks(): List<AllTracksEntity>

    @Query("SELECT * FROM all_tracks_table WHERE trackId IN (:trackIds)")
    suspend fun getTracksByIds(trackIds: List<Int?>): List<TrackEntity>

    @Delete
    suspend fun deleteTrackInAllTracksEntity(track: AllTracksEntity)

    @Query("SELECT * FROM all_tracks_table WHERE trackId IN (:trackIds)")
    suspend fun getTrackById(trackIds: Int?): TrackEntity

}