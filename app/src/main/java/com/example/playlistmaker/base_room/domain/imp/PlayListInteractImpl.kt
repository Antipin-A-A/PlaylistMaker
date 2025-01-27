package com.example.playlistmaker.base_room.domain.imp

import com.example.playlistmaker.base_room.domain.api.PlayListInteract
import com.example.playlistmaker.base_room.domain.api.PlayListReposytory
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractImpl(
    private val playListReposytory: PlayListReposytory
) : PlayListInteract {
    override suspend fun savePlayList(playList: PlayList) {
        playListReposytory.savePlayList(playList)
    }

    override suspend fun updatePlayList(playList: PlayList) {
        playListReposytory.updatePlayList(playList)
    }

    override suspend fun getPlayList(): Flow<List<PlayList>> {
        return playListReposytory.getPlayList()
    }

    override suspend fun getPlayListById(playlistId: Int): PlayList? {
        return playListReposytory.getPlayListById(playlistId)
    }

    override suspend fun insertInTableAllTracks(tracks: Track) {
        playListReposytory.insertInTableAllTracks(tracks)
    }

}