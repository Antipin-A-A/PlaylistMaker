package com.example.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.base_room.domain.api.PlayListInteract
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playListInteract: PlayListInteract
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayListState>()

    fun observeState(): LiveData<PlayListState> = stateLiveData

    fun fillData() {
        renderState(PlayListState.Loading)
        viewModelScope.launch {
            playListInteract
                .getPlayList()
                .collect { playlist ->
                    processResult(playlist, message = String.toString())
                }
        }
    }

    private fun processResult(playlist: List<PlayList>, message: String) {
        if (playlist.isEmpty()) {
            renderState(PlayListState.Empty(message))
        } else {
            renderState(PlayListState.Content(playlist.reversed()))
        }
    }

    private fun renderState(state: PlayListState) {
        stateLiveData.postValue(state)
    }
}

