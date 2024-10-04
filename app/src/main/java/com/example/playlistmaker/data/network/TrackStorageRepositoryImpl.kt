package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.dto.TrackStorage
import com.example.playlistmaker.domain.api.TrackStorageRepository
import com.example.playlistmaker.domain.modeles.Track

class TrackStorageRepositoryImpl(private val trackStorage: TrackStorage) : TrackStorageRepository {

    override fun saveTrack(trackParam: List<Track>) {
        val trackDto = trackParam.map {
            TrackDto(
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.trackId,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
        trackStorage.saveAllTrackList(trackDto)
    }

    override fun getTrack(): List<Track> {
        val trackDto = trackStorage.getTrack()
        return trackDto.map {
            Track(
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.trackId,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
    }

}
