package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.base_room.domain.api.RoomInteract
import com.example.playlistmaker.search.Object.CONNECT_OK
import com.example.playlistmaker.search.Object.ERROR_CONNECT
import com.example.playlistmaker.search.Object.ERROR_FILE_NOT_FOUND
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.mapper.toDomainModel
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val roomInteract: RoomInteract
) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            ERROR_CONNECT -> {
                emit(Resource.Error("$ERROR_CONNECT"))
            }

            CONNECT_OK -> {
                with(response as TrackResponse) {
                    val stored = roomInteract.getTracksRoom().first()
                    val favoriteIds = stored.map { it.trackId }.toSet()
                    val result = Resource.Success(results.map {
                        it.toDomainModel().copy(isFavorite = favoriteIds.contains(it.trackId))
                    })
                    emit(result)
                }
            }

            else -> {
                emit(Resource.Error("$ERROR_FILE_NOT_FOUND"))
            }
        }
    }
}


