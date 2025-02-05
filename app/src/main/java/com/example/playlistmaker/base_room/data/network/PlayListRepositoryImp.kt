package com.example.playlistmaker.base_room.data.network

import com.example.playlistmaker.base_room.data.bd.AppDataBase
import com.example.playlistmaker.base_room.data.bd.PlayListEntity
import com.example.playlistmaker.base_room.data.convector.PlayListDbConvector
import com.example.playlistmaker.base_room.data.convector.TrackDbConvertor
import com.example.playlistmaker.base_room.domain.api.PlayListReposytory
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mapToData
import mapToDomain

class PlayListRepositoryImp(
    private val appDataBase: AppDataBase,
    private val playListDbConvector: PlayListDbConvector,
    private val trackDbConvertor: TrackDbConvertor
) : PlayListReposytory {
    override suspend fun savePlayList(playList: PlayList) {
        appDataBase.playListDao().insertPlayList(playListDbConvector.mapToData(playList))
    }

    override suspend fun updatePlayList(playList: PlayList) {
        appDataBase.playListDao().updatePlayList(playListDbConvector.mapToData(playList))
    }

    override suspend fun getAllPlayList(): Flow<List<PlayList>> = flow {
        val result = appDataBase.playListDao().getAllPlayList()
        emit(convertFromPlayListEntity(result))
    }

    override suspend fun getPlayListById(playlistId: Int): PlayList {
        val playListEntity = appDataBase.playListDao().getPlayListById(playlistId)
        return playListEntity.let { playListDbConvector.mapToDomain(it) }
    }

    override suspend fun insertInTableAllTracks(tracks: Track) {
        val listTracks = appDataBase.allTracksDao().getTracks().map { it.mapToDomain() }
        saveTrack(listTracks, tracks)
    }

    override suspend fun getAllTrack(): Flow<List<Track>> = flow {
        val result = appDataBase.allTracksDao().getTracks()
        emit(result.map { it.mapToDomain() })
    }

    override suspend fun getTracksByIds(listId: List<Int>): List<Track>{
        val result = appDataBase.allTracksDao().getTracksByIds(listId)
       return result.map { trackDbConvertor.mapToDomain(it) }
    }

    override suspend fun deleteTrackInAllTracksEntity(tracks: Track) {
        appDataBase.allTracksDao().deleteTrackInAllTracksEntity(tracks.mapToData())
    }

    override suspend fun getTrackById(trackId: Int?): Track {
        val trackIdEntity = appDataBase.allTracksDao().getTrackById(trackId)
        return trackIdEntity.let { trackDbConvertor.mapToDomain(it) }
    }

    override suspend fun deletePlayList(playList: PlayList) {
        appDataBase.playListDao().deletePlaylist(playListDbConvector.mapToData(playList))
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