package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.base_room.data.bd.AppDataBase
import com.example.playlistmaker.base_room.data.bd.TrackDao
import com.example.playlistmaker.base_room.data.convector.TrackDbConvertor
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.mapper.toDomainModel
import com.example.playlistmaker.search.Object.CONNECT_OK
import com.example.playlistmaker.search.Object.ERROR_CONNECT
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.Object.ERROR_FILE_NOT_FOUND
import com.example.playlistmaker.search.data.model.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDataBase: AppDataBase,
) : TrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            ERROR_CONNECT -> {
                emit(Resource.Error("$ERROR_CONNECT"))
            }

            CONNECT_OK -> {
                with(response as TrackResponse) {
                    val trackIdRoom = appDataBase.trackDao().getIdTrack()
                    val result = Resource.Success(results.map {
                        it.toDomainModel()
                    })
 //                   checkId(trackIdRoom, result)
                    emit(result)
                }
            }

            else -> {
                emit(Resource.Error("$ERROR_FILE_NOT_FOUND"))
            }
        }

    }

//    private fun checkId(tracksIdFavorites: List<Int>, result: Resource<List<Track>>) {
//        tracksIdFavorites.forEach { idFavorites ->
//            result.data?.forEach { track ->
//                if (idFavorites.equals(track)) {
//                    track.isFavorite = true
//                }
//                else{
//                    track.isFavorite = false
//                }
//            }
//        }
//    }

}


