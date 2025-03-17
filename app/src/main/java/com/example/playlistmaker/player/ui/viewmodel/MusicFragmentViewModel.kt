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
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MusicFragmentViewModel(
    private val mediaPlayerInteract: MediaPlayerInteract,
    private val trackIteractor: TrackIteractor,
    private val roomInteract: RoomInteract,
    private val playListInteract: PlayListInteract
) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val stateLiveData = MutableLiveData<PlayListStateForMusic>()
    fun observeState(): LiveData<PlayListStateForMusic> = stateLiveData

    private var timerJob: Job? = null

    private val stateMutable = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> = stateMutable

    private var screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    init {
        preparePlayer()
        getTrack()
        checkIsTrackFavorite()
        _isFavorite.value = false
        fillData()
    }

//    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
//        this.audioPlayerControl = audioPlayerControl
//
//        viewModelScope.launch {
//            audioPlayerControl.getPlayerState().collect {
//                screenStateLiveData.postValue(it)
//            }
//        }
//    }
//
//    fun onPlayerButtonClicked() {
//        _isPlaying.value = !_isPlaying.value!!
//        if (_isPlaying.value!!) {
//           start()
//            Log.i("Log1", "${audioPlayerControl?.startPlayer()},_isPlaying = ${_isPlaying.value} ")
//
//        } else {
//           pause()
//            Log.i("Log2", "${audioPlayerControl?.pausePlayer()}, _isPlaying = ${_isPlaying.value} ")
//        }
//       // setOnCompleted()
//        Log.i("Log3", " _isPlaying = ${_isPlaying.value} ")
//        /*        if (playerState.value is PlayerState.Playing) {
//                    audioPlayerControl?.pausePlayer()
//                } else {
//                    audioPlayerControl?.startPlayer()
//                }*/
//    }
//    fun start(){
//        audioPlayerControl?.startPlayer()
//    }
//
//    fun pause() {
//        _isPlaying.value = false
//        audioPlayerControl?.setOnCompleted()
//        Log.i("Log4", " _isPlaying = ${_isPlaying.value} ")
//    }
//
//    fun time(): String {
//        val time = audioPlayerControl?.time().toString()
//        Log.i("Log5", " _isPlaying = ${_isPlaying.value} , time = $time")
//        return time
//    }
//
//     fun setOnCompleted() {
//        _isPlaying.value = false
//        audioPlayerControl?.setOnCompleted()
//        Log.i("LogComlet", " _isPlaying = ${_isPlaying.value} ")
//    }
//    fun removeAudioPlayerControl() {
//        audioPlayerControl = null
//    }

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

    private fun getTrack() {
        screenStateLiveData.postValue(TrackScreenState.Content(trackIteractor.loadTrackData()))
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value!!
        if (_isPlaying.value!!) {
            start()
            Log.i("Log3" ,"pause, isRunTime =${_isPlaying.value}")
        } else {
            pause()
            Log.i("Log4" ,"pause, isRunTime =${_isPlaying.value}")
        }
        setOnCompleted()
    }

    private fun preparePlayer() {
        val url = trackIteractor.loadTrackData().previewUrl.toString()
        if (url != "null") {
            stateMutable.value = PlayerState.PreparePlayer(mediaPlayerInteract.preparePlayer(url))
        }
    }

    fun pause() {
        stateMutable.value = PlayerState.Pause(mediaPlayerInteract.pausePlayer())
        timerJob?.cancel()
        _isPlaying.value = false
    }

    private fun start() {
        stateMutable.value = PlayerState.Play(mediaPlayerInteract.play())
        startTime()
    }

    private fun position(): Long {
        return mediaPlayerInteract.currentPosition()
    }

    private fun release() {
        stateMutable.value = PlayerState.Release(mediaPlayerInteract.release())
    }

    private fun setOnCompleted() {
        stateMutable.value =
            PlayerState.SetOnComplete(mediaPlayerInteract.setOnCompletionListener {
                timerJob?.cancel()
                screenStateLiveData.postValue(TrackScreenState.Complete)
                _isPlaying.value = false
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
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(position()) ?: "00:00"
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
        super.onCleared()
     //   audioPlayerControl = null
            release()
    }

    companion object {
        private const val DELAY = 300L
    }
}
