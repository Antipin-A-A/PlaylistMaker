package com.example.playlistmaker.player.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.base_room.domain.api.PlayListInteract
import com.example.playlistmaker.base_room.domain.api.RoomInteract
import com.example.playlistmaker.player.domain.api.interact.MediaPlayerInteract
import com.example.playlistmaker.player.service.AudioPlayerControl
import com.example.playlistmaker.player.ui.state.PlayerState
import com.example.playlistmaker.player.ui.state.TimeTrack
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicFragmentViewModel(
    private val trackIteractor: TrackIteractor,
    private val roomInteract: RoomInteract,
    private val playListInteract: PlayListInteract
) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val stateLiveData = MutableLiveData<PlayListStateForMusic>()
    fun observeState(): LiveData<PlayListStateForMusic> = stateLiveData

//    private var timerJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>()
    val observePlayerState: LiveData<PlayerState> = _playerState

    private val _currentPosition = MutableLiveData<TimeTrack>()
    val currentPosition: LiveData<TimeTrack> = _currentPosition


    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect { state ->
                _playerState.postValue(state)
                _isPlaying.value = when (state) {
                    PlayerState.PLAYING -> true
                    PlayerState.PAUSED, PlayerState.PREPARED -> false
                    else -> _isPlaying.value
                }
            }
        }

        viewModelScope.launch {
            audioPlayerControl.getPlayerTime().collect {
                _currentPosition.postValue(it)
            }
        }
    }

    init {
        checkIsTrackFavorite()
        _isFavorite.value = false
        fillData()
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value!!
        if (_isPlaying.value!!) {
            audioPlayerControl?.startPlayer()
        } else {
            pause()
        }
    }

    fun pause(){
        audioPlayerControl?.pausePlayer()
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    fun showNotification() {
        audioPlayerControl?.provideNotificator()
    }

    fun hideNotification() {
        audioPlayerControl?.stopNotification()
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                _isFavorite.value = false
                deleteListTrack(roomInteract.getTracksRoom(), trackIteractor.loadTrackData())
                roomInteract.deleteTrackRoom(trackIteractor.loadTrackData())
            } else {
                trackIteractor.loadTrackData().isFavorite = true
                roomInteract.saveTrackRoom(trackIteractor.loadTrackData())
                _isFavorite.value = true
            }
        }
    }

    private fun checkIsTrackFavorite() {
        viewModelScope.launch {
            check(roomInteract.getTracksRoom(), trackIteractor.loadTrackData())
        }
    }

    private suspend fun check(resultList: Flow<List<Track>>, track: Track): Boolean? {
        resultList.collect { idFavorites ->
            idFavorites.forEach {
                if (track.trackId == it.trackId) {
                    track.isFavorite = true
                    _isFavorite.value = true
                }
            }
        }
        return _isFavorite.value
    }

    private suspend fun deleteListTrack(resultList: Flow<List<Track>>, track: Track) {
        resultList.collect { idFavorites ->
            idFavorites.forEach {
                if (track.trackId == it.trackId) {
                    track.id = it.id
                    roomInteract.deleteTrackRoom(track)
                } else {
                    roomInteract.deleteTrackRoom(track)
                }
            }
        }
    }

    fun getTrack(): Track {
        return trackIteractor.loadTrackData()
    }

    fun fillData() {
        renderState(PlayListStateForMusic.Loading)
        viewModelScope.launch {
            playListInteract
                .getAllPlayList()
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    private fun processResult(playlist: List<PlayList>) {
        renderState(PlayListStateForMusic.Content(playlist.reversed()))
    }


    private fun renderState(state: PlayListStateForMusic) {
        stateLiveData.postValue(state)
    }

    fun saveFillData(playlistId: Int) {
        viewModelScope.launch {
            addTrackToPlaylist(playlistId)
        }
    }

    private suspend fun addTrackToPlaylist(playlistId: Int) {
        val trackId = trackIteractor.loadTrackData().trackId?.toInt()!!

        val playList = playListInteract.getPlayListById(playlistId)
        playListInteract.insertInTableAllTracks(trackIteractor.loadTrackData())

        val trackIds = playList.listTracksId?.toMutableList() ?: mutableListOf()

        if (trackIds.contains(trackId)) {
            _message.value = "Трек уже добавлен в плейлист ${playList.listName}"
        } else {
            trackIds.add(trackId)
            val updatedPlayList = playList.copy(
                listTracksId = trackIds,
                countTracks = trackIds.size
            )
            playListInteract.updatePlayList(updatedPlayList)
            _message.value = "Трек добавлен в плейлист ${playList.listName}"
        }
    }

    override fun onCleared() {
        audioPlayerControl = null
    }

}

