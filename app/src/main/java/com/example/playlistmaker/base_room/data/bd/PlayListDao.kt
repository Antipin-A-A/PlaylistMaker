package com.example.playlistmaker.base_room.data.bd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayListDao {
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePlayList(playList: PlayListEntity)

    @Query("SELECT * FROM playlist")
    suspend fun getAllPlayList(): List<PlayListEntity>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlayListById(playlistId: Int): PlayListEntity

    @Delete(entity = PlayListEntity::class)
    suspend fun deletePlaylist(playList: PlayListEntity)

    @Query("DELETE FROM playlist WHERE id = :playlistId")
    suspend fun deletePlaylistId(playlistId: Int)

}