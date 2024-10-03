package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.modeles.Track


interface TrackRepository {
    fun searchTracks(expression: String): List<Track>
}
