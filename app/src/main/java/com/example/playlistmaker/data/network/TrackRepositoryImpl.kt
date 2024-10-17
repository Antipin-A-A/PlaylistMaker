package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.toDomainModel
import com.example.playlistmaker.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.domain.modeles.Track
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as TrackResponse).results.map { it.toDomainModel() })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}


