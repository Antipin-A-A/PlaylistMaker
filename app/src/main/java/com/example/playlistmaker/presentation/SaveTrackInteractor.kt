package com.example.playlistmaker.presentation

import com.example.playlistmaker.domain.api.TrackStorageRepository
import com.example.playlistmaker.domain.modeles.Track

class SaveTrackInteractor(private val trackStorageRepository: TrackStorageRepository) {
    fun execute(track: MutableList<Track>) {
        trackStorageRepository.saveTrack(track)
    }
}