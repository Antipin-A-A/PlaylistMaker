package com.example.playlistmaker.search.domain.api.reposirory

import androidx.core.content.edit
import com.example.playlistmaker.base_room.data.bd.PlayListEntity
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.data.shared_preference.NEW_FACT_TRACK_KEY
import com.example.playlistmaker.search.domain.modeles.Track
import com.google.gson.Gson

interface TrackStorageRepository {
    fun saveTrackList(track: Track)
    fun saveTrack(track: Track)
    fun loadTracksList(): List<Track>
    fun removeTrack()
    fun loadTrackData(): Track
    fun savePlaylist(playlist: PlayList)
    fun getPlaylistDto(): PlayList

}