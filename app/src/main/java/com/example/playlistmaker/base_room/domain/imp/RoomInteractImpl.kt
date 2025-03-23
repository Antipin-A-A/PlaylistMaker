package com.example.playlistmaker.base_room.domain.imp

import com.example.playlistmaker.base_room.domain.api.RoomInteract
import com.example.playlistmaker.base_room.domain.api.RoomRepository
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow

class RoomInteractImpl(
    private val roomRepository: RoomRepository
): RoomInteract {
    override suspend fun saveTrackRoom(track: Track) {
       roomRepository.saveTrackRoom(track)
    }

    override suspend fun deleteTrackRoom(track: Track) {
        roomRepository.deleteTrackRoom(track)
    }

    override suspend fun getTracksRoom(): Flow<List<Track>> {
        return roomRepository.getTracksRoom()
    }
}