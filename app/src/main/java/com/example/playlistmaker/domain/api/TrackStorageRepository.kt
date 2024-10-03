package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.modeles.Track

interface TrackStorageRepository {

    fun saveTrack(trackParam: List<Track>)

    fun getTrack():List<Track>

    fun removeAllTrackList()
}