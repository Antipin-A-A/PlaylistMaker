package com.example.playlistmaker.base_room.domain.api

import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow

interface RoomInteract {
    suspend fun saveTrackRoom(track: Track)
    suspend fun deleteTrackRoom(track: Track)
    suspend fun getTracksRoom(): Flow<List<Track>>
}