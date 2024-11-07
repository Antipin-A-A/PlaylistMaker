package com.example.playlistmaker.player.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor

class MusicActivityViewModel(
    private val trackIteractor: TrackIteractor,
) : ViewModel() {

    private val mediaPlayerInteract by lazy { Creator.provideMediaPlayerInteractor() }

    private val stateMutable = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = stateMutable

    private var screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData

    init {
        preparePlayer()
        getTrack()
    }

    private fun getTrack() {
        screenStateLiveData.postValue(TrackScreenState.Content(trackIteractor.loadTrackData()))
    }



    private fun preparePlayer() {
        val url = trackIteractor.loadTrackData().previewUrl.toString()
        stateMutable.value = PlayerState.preparePlayer(mediaPlayerInteract.preparePlayer(url))
    }

    fun pause() {
        stateMutable.value = PlayerState.Pause(mediaPlayerInteract.pausePlayer())
    }

    fun start() {
        stateMutable.value = PlayerState.Play(mediaPlayerInteract.play())
    }

    fun position(): Long {
        return mediaPlayerInteract.currentPosition()
    }

    fun release() {
        stateMutable.value = PlayerState.Release(mediaPlayerInteract.release())
        Log.i("Log", "MusicActivityViewModel release")
    }

    fun setOnComplited(function: () -> Unit) {
        stateMutable.value =
            PlayerState.SetOnComplet(mediaPlayerInteract.setOnCompletionListener {
                function()
            })
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

    companion object {
        fun mediaViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MusicActivityViewModel(
                    trackIteractor = Creator.provideTrackInteractor(),
                )
            }
        }
    }
}
