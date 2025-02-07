package com.example.playlistmaker.screenplaylist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.base_room.domain.api.PlayListInteract
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.sharing.domain.api.interact.ExternalNavigator
import kotlinx.coroutines.launch

class ScreenPlaylistViewModel(
    private val trackIteractor: TrackIteractor,
    private val playListInteract: PlayListInteract,
    private val externalNavigator: ExternalNavigator
) : ViewModel() {
    private var idPlaylist: Int = 0
    private lateinit var playList: PlayList
    private lateinit var currentPlayListTracks: List<Track>

    private val stateTrackLiveData = MutableLiveData<TrackListStateScreen>()
    fun observeTrackState(): LiveData<TrackListStateScreen> = stateTrackLiveData

    private val statePlayListLiveData = MutableLiveData<PlayListScreenState>()
    fun observePlayListState(): LiveData<PlayListScreenState> = statePlayListLiveData

    private val stateString = MutableLiveData<String>()
    fun observeString(): LiveData<String> = stateString


    init {
        getPlaylist()
    }


    fun saveTrack(track: Track) {
        trackIteractor.saveTrack(track)
    }

    fun getPlaylist() {
        renderStatePlayList(PlayListScreenState.Loading)
        renderState(TrackListStateScreen.Loading)
        viewModelScope.launch {
            resultPlaylist()
            resultTrack()
        }
    }

    private suspend fun resultPlaylist() {
        idPlaylist = trackIteractor.getCurrentPlaylist().id
        playList = playListInteract.getPlayListById(idPlaylist)
        renderStatePlayList(PlayListScreenState.Content(playList))
    }

    fun deleteTrack(idtrack: String) {
        viewModelScope.launch {
            deleteTrackIfNotInPlaylists(idtrack)
            getPlaylist()
        }
    }
    private suspend fun resultTrack() {
        playList.let {
            it.listTracksId?.let { it1 -> loadTracks(it1) }
        }
    }

    private fun renderStatePlayList(state: PlayListScreenState) {
        statePlayListLiveData.postValue(state)
    }

    private suspend fun loadTracks(trackIds: List<Int?>) {
        currentPlayListTracks = playListInteract.getTracksByIds(trackIds.filterNotNull())
        val totalDurationInSeconds = currentPlayListTracks.sumOf { track ->
            val (minutes, seconds) = track.trackTimeMillis!!.split(":").map { it.toInt() }
            minutes * 60 + seconds
        }
        val totalMinutes = totalDurationInSeconds / 60
        stateString.postValue(totalMinutes.toString())
        if (currentPlayListTracks.isEmpty()) {
            renderState(TrackListStateScreen.Empty)
        } else {
            renderState(TrackListStateScreen.Content(currentPlayListTracks.reversed()))
        }
    }

    private fun renderState(state: TrackListStateScreen) {
        stateTrackLiveData.postValue(state)
    }
    //удалить трек
    private suspend fun deleteTrackIfNotInPlaylists(trackId: String) {
        val playlists = playListInteract.getAllPlayList()
        playlists.collect { listTracksId ->
            val isTrackInAnyPlaylist = listTracksId.any { playlistId ->
                playlistId.listTracksId?.contains(trackId.toInt()) == true
            }
            val trackIds = playList.listTracksId?.toMutableList() ?: mutableListOf()

            if (!isTrackInAnyPlaylist) {
                val trackToDelete = playListInteract.getTrackById(trackId.toInt())
                playListInteract.deleteTrackInAllTracksEntity(trackToDelete)
            }
            trackIds.remove(trackId.toInt())
            val updatedPlayList = playList.copy(
                listTracksId = trackIds,
                countTracks = trackIds.size
            )
            playListInteract.updatePlayList(updatedPlayList)
        }
    }

    fun deleteList() {
        viewModelScope.launch {
            playListInteract.deletePlayList(playList)
        }
    }

    fun sharePlayList() {
        externalNavigator.shareLink(messageGenerator())
    }

    private fun messageGenerator(): String {

        val message = buildString {
            this.append("плейлист ${playList.listName}")
            if (!playList.description.isNullOrEmpty()) {
                this.append("\n${playList.description}\n")
            } else this.append("\n")
            this.append("${playList.countTracks} треков"
            )
            currentPlayListTracks.forEachIndexed { index, track ->
                this.append("\n${(index + 1)}")
                this.append(". ${track.artistName} - ")
                this.append(track.trackName)
                this.append(" (${track.trackTimeMillis})")
            }
        }
        return message
    }
}



