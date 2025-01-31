package com.example.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.base_room.domain.api.RoomInteract
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val roomInteract: RoomInteract,
    private val trackIteractor: TrackIteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()

    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun fillData() {
        renderState(FavoriteState.Loading)
        viewModelScope.launch {
            roomInteract
                .getTracksRoom()
                .collect { track ->
                    processResult(track, message = String.toString())
                }
        }
    }

    private fun processResult(tracks: List<Track>, message: String) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty(message))
        } else {
            renderState(FavoriteState.Content(tracks.reversed()))
        }
    }

    private fun renderState(state: FavoriteState) {
        stateLiveData.postValue(state)
    }

    fun saveTrack(track: Track) {
        trackIteractor.saveTrack(track)
    }
}