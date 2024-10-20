package com.example.playlistmaker.domain.api.reposirory

import com.example.playlistmaker.domain.modeles.Track
import com.example.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(expression: String):Resource<List<Track>>
}
