package com.example.playlistmaker.playlist.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.base_room.domain.api.PlayListInteract
import com.example.playlistmaker.playlist.domain.model.PlayList
import kotlinx.coroutines.launch

class NewPlayListViewModel(
    private val playListInteract: PlayListInteract
) : ViewModel() {

    fun savePlayList(listName: String, description: String?, urlImage: Uri?) {
        viewModelScope.launch {
            playListInteract.savePlayList(
                PlayList(
                    id = 0,
                    listName = listName,
                    description = description,
                    urlImage = urlImage,
                    listTracksId = mutableListOf(),
                    countTracks = 0
                )
            )
        }
    }

//    fun updatePlaylist(playlistId: Int?, listName: String, description: String?, urlImage: Uri?) {
//        // Логика обновления плейлиста
//    }

   fun updatePlaylist(
        playlistId: Int,
        listName: String,
        description: String?,
        urlImage: Uri?
    ) {
        //  val trackId = trackIteractor.loadTrackData().trackId?.toInt()!!
        viewModelScope.launch {
            val playList = playListInteract.getPlayListById(playlistId)
            //  playListInteract.insertInTableAllTracks(trackIteractor.loadTrackData())

//            val trackIds = playList.listTracksId?.toMutableList() ?: mutableListOf()
//
//            if (trackIds.contains(trackId)) {
//                _message.value = "Трек уже добавлен в плейлист ${playList.listName}"
//            } else {
//                trackIds.add(trackId)
            val updatedPlayList = playList.copy(
                id = playlistId,
                listName = listName,
                description = description,
                urlImage = urlImage,
            )
            playListInteract.updatePlayList(updatedPlayList)
        }
    }
}

