package com.example.playlistmaker.base_room.data.network

import com.example.playlistmaker.base_room.data.bd.AppDataBase
import com.example.playlistmaker.base_room.data.bd.PlayListEntity
import com.example.playlistmaker.base_room.data.convector.PlayListDbConvector
import com.example.playlistmaker.base_room.domain.api.PlayListReposytory
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mapToData
import mapToDomain

class PlayListRepositoryImp(
    private val appDataBase: AppDataBase,
    private val playListDbConvector: PlayListDbConvector

) : PlayListReposytory {
    override suspend fun savePlayList(playList: PlayList) {
        appDataBase.playListDao().insertPlayList(playListDbConvector.mapToData(playList))
    }

    override suspend fun updatePlayList(playList: PlayList) {
        appDataBase.playListDao().updatePlayList(playListDbConvector.mapToData(playList))
    }

    override suspend fun getPlayList(): Flow<List<PlayList>> = flow {
        val result = appDataBase.playListDao().getPlayList()
        emit(convertFromPlayListEntity(result))
    }

    override suspend fun getPlayListById(playlistId: Int): PlayList? {
        val playListEntity = appDataBase.playListDao().getPlayListById(playlistId)
        return playListEntity?.let { playListDbConvector.mapToDomain(it) }
    }

    override suspend fun insertInTableAllTracks(tracks: Track) {
        val listTracks = appDataBase.allTracksDao().getTracks().map { it.mapToDomain() }
        saveTrack(listTracks, tracks)
    }

    private fun convertFromPlayListEntity(playlistEntity: List<PlayListEntity>): List<PlayList> {
        return playlistEntity.map { playlist -> playListDbConvector.mapToDomain(playlist) }
    }

    private suspend fun saveTrack(playList: List<Track>, track: Track) {
        val exists = playList.any { it.trackId == track.trackId }
        if (!exists) {
            val newTrack = track.mapToData()
            appDataBase.allTracksDao().insertInTableAllTracks(newTrack)
        }

    }
}