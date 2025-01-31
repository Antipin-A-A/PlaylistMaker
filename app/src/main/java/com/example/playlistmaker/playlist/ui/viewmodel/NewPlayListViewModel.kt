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
}