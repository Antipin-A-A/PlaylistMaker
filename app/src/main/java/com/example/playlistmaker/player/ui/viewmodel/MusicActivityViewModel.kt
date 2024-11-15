package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.interact.MediaPlayerInteract
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor

class MusicActivityViewModel(
    private val mediaPlayerInteract: MediaPlayerInteract,
    private val trackIteractor: TrackIteractor,
) : ViewModel() {

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

    fun preparePlayer() {
        val url = trackIteractor.loadTrackData()?.previewUrl.toString()
        if (url != "null") {
            stateMutable.value = PlayerState.PreparePlayer(mediaPlayerInteract.preparePlayer(url))
        }
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

    private fun release() {
        stateMutable.value = PlayerState.Release(mediaPlayerInteract.release())
    }

    fun setOnCompleted() {
        stateMutable.value =
            PlayerState.SetOnComplete(mediaPlayerInteract.setOnCompletionListener {
                screenStateLiveData.postValue(TrackScreenState.Complete)
            })
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

}
