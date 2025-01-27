package com.example.playlistmaker.base_room.domain.api

import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteract {
    suspend fun savePlayList(playList: PlayList)
    suspend fun updatePlayList(playList: PlayList)
    suspend fun getPlayList(): Flow<List<PlayList>>
    suspend fun getPlayListById(playlistId: Int): PlayList?
    suspend fun insertInTableAllTracks(tracks: Track)
}