package com.example.playlistmaker.presentation

import com.example.playlistmaker.domain.api.TrackStorageRepository
import com.example.playlistmaker.domain.modeles.Track

class GetTrackUseCase(private val trackStorageRepository: TrackStorageRepository) {
    fun execute(): List<Track> {
        return trackStorageRepository.getTrack()
    }
}

