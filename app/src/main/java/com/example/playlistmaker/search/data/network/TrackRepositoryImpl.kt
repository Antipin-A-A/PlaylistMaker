package com.example.playlistmaker.search.data.network

import android.util.Log
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.mapper.toDomainModel
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
                Log.i("Log-TrackRepositoryImpl -1", "${response.resultCode}")
            }

            200 -> {
                with(response as TrackResponse) {
                    emit(Resource.Success(results.map { it.toDomainModel() }))
                }
                Log.i("Log-TrackRepositoryImpl -2", "${response.resultCode}")
            }
            else -> {
               emit(Resource.Error("Ошибка сервера"))
                Log.i("Log-TrackRepositoryImpl -3", "${response.resultCode}")
            }
        }
    }

}


