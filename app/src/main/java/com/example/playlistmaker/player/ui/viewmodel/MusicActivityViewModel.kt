package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.interact.MediaPlayerInteract
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicActivityViewModel(
    private val mediaPlayerInteract: MediaPlayerInteract,
    private val trackIteractor: TrackIteractor,
) : ViewModel() {

    private var timerJob: Job? = null

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
        val url = trackIteractor.loadTrackData()?.previewUrl.toString()
        if (url != "null") {
            stateMutable.value = PlayerState.PreparePlayer(mediaPlayerInteract.preparePlayer(url))
        }
    }

    fun pause() {
        stateMutable.value = PlayerState.Pause(mediaPlayerInteract.pausePlayer())
        timerJob?.cancel()
    }

    fun start() {
        stateMutable.value = PlayerState.Play(mediaPlayerInteract.play())
        startTime()
    }

    private fun position(): Long {
        return mediaPlayerInteract.currentPosition()
    }

    private fun release() {
        stateMutable.value = PlayerState.Release(mediaPlayerInteract.release())
    }

    fun setOnCompleted() {
        stateMutable.value =
            PlayerState.SetOnComplete(mediaPlayerInteract.setOnCompletionListener {
                timerJob?.cancel()
                screenStateLiveData.postValue(TrackScreenState.Complete)
            })
    }

    private fun startTime() {
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteract.isPlaying()) {
                delay(DELAY)
                screenStateLiveData.postValue(TrackScreenState.TimeTrack(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(position())?: "00:00"
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }
    companion object {
        private const val DELAY = 300L
    }
}
