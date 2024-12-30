package com.example.playlistmaker.search.domain.api.reposirory

import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.data.network.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String):Flow<Resource<List<Track>>>
}
