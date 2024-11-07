package com.example.playlistmaker.search.domain.api.reposirory

import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.data.network.Resource

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}
