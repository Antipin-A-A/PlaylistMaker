package com.example.playlistmaker.domain.api.reposirory

import com.example.playlistmaker.domain.modeles.Track

interface TrackStorageRepository {
    fun saveTrack(track: Track)
    fun getSavedTracks(): List<Track>
    fun removeTrack()
}