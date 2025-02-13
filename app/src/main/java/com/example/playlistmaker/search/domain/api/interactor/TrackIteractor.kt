package com.example.playlistmaker.search.domain.api.interactor

import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow

interface TrackIteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?,String?>>

    fun saveTrackList(track: Track)

    fun saveTrack(track: Track)

    fun loadTracksList(): List<Track>

    fun removeTrackList()

    fun loadTrackData(): Track

    fun savePlaylist(playlist: PlayList)

    fun getCurrentPlaylist(): PlayList
}