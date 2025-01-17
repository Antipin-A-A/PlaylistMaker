package com.example.playlistmaker.search.domain.api.reposirory

import com.example.playlistmaker.search.domain.modeles.Track

interface TrackStorageRepository {
    fun saveTrackList(track: Track)
    fun saveTrack(track: Track)
    fun loadTracksList(): List<Track>
    fun removeTrack()
    fun loadTrackData(): Track
}