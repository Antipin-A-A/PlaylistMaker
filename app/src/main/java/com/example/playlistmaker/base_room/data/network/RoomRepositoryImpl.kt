package com.example.playlistmaker.base_room.data.network

import android.util.Log
import com.example.playlistmaker.base_room.data.bd.AppDataBase
import com.example.playlistmaker.base_room.data.bd.TrackEntity
import com.example.playlistmaker.base_room.data.convector.TrackDbConvertor
import com.example.playlistmaker.base_room.domain.api.RoomRepository
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val trackDbConvertor: TrackDbConvertor
) : RoomRepository {

    override suspend fun saveTrackRoom(track: Track) {
        appDataBase.trackDao().insertTrack(trackDbConvertor.mapToData(track))
    }

    override suspend fun deleteTrackRoom(track: Track) {
        appDataBase.trackDao().deleteTrack(trackDbConvertor.mapToData(track))
    }

    override suspend fun getTracksRoom(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getTracks()
        emit(convertFromTracksListEntity(tracks))
    }

    private fun convertFromTracksListEntity(movies: List<TrackEntity>): List<Track> {
        return movies.map { track -> trackDbConvertor.mapToDomain(track) }
    }
}

