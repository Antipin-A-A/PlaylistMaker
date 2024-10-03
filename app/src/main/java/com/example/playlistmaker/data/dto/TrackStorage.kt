package com.example.playlistmaker.data.dto


interface TrackStorage {

    fun saveAllTrackList(trackDto: List<TrackDto>)

    fun getTrack(): List<TrackDto>

    fun removeAllTrackList()
}