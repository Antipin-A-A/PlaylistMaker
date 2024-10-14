package com.example.playlistmaker.data.shared_preference

import com.example.playlistmaker.data.mapper.toDataModel
import com.example.playlistmaker.data.mapper.toDomainModel
import com.example.playlistmaker.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.domain.modeles.Track

class TrackStorageRepositoryImpl(private val sharedManager: SharedManager) :
    TrackStorageRepository {

    override fun saveTrack(track: Track) {
        val trackDto = track.toDataModel()
        sharedManager.saveTrackList(trackDto)
    }

    override fun getSavedTracks(): List<Track> {
        return sharedManager.getTrackDtoList().map { it.toDomainModel() }
    }

    override fun removeTrack() {
        sharedManager.removeTrackList()
    }
}