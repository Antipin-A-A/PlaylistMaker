package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.toDomainModel
import com.example.playlistmaker.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.domain.modeles.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return if (response.resultCode == 200) {
            (response as TrackResponse).results.map { it.toDomainModel() }
        } else {
            emptyList()
        }
    }

}


