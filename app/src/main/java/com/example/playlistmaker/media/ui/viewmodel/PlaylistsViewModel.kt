package com.example.playlistmaker.media.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.base_room.domain.api.PlayListInteract
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListState
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playListInteract: PlayListInteract,
    private val trackIteractor: TrackIteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayListState>()
    val state:LiveData<PlayListState> = stateLiveData

    fun fillData() {
        Log.i("LogViewModel-1", "fillData()")
        renderState(PlayListState.Loading)
        viewModelScope.launch {
            playListInteract
                .getAllPlayList()
                .collect { playlist ->
                    processResult(playlist, message = String.toString())
                    Log.i("LogViewModel-4", "playlistSize - ${playlist.size}")
                }
        }
    }

    private fun processResult(playlist: List<PlayList>, message: String) {
        if (playlist.isEmpty()) {
            renderState(PlayListState.Empty(message))
        } else {
            Log.i("LogViewModel-2", "playlistsize = ${playlist.size}")
            renderState(PlayListState.Content(playlist.reversed()))
        }
    }

    private fun renderState(state: PlayListState) {
        stateLiveData.postValue(state)
    }

    fun saveTrack(playList: PlayList) {
        trackIteractor.savePlaylist(playList)
    }
    fun deleteList(playList: PlayList) {
        Log.i("Log-delete-1", "delete id -")
        viewModelScope.launch {
          //  val playList = playListInteract.getPlayListById(idPlaylist)
            Log.i("Log-delete-2", "delete id - ${playList.id}, name = ${playList.listName}")
            deletePlayList(playList)
            fillData()
        }

    }

    private suspend fun deletePlayList(playList: PlayList) {
        Log.i("Log-delete-3", ", playlistid = ${playList.id}")
        playListInteract.deletePlayList(playList)
//        allPlaylist.collect { idFavorites ->
//            idFavorites.forEach {
//                if (playList.id == it.id) {
//                    Log.i("Log2", "allPlaylist = ${playList.id}, id = ${it.id} ")
//                    playList.id = it.id
//                    Log.i("log 3", "trackToDelete = $playlistId, playList.id = ${playList.id}")// Удаляем трек
//                    playListInteract.deletePlayList(playList)
//                } else {
//                    playListInteract.deletePlayList(playList)
//                }
//            }
//        }
    }
}

