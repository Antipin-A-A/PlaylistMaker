package com.example.playlistmaker.search.domain.api.interactor

import com.example.playlistmaker.search.domain.modeles.Track

interface TrackIteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTreks: List<Track>?, errorMessage: String?)
    }

    fun saveTrackList(track: Track)

    fun saveTrack(track: Track)

    fun loadTracksList(): List<Track>

    fun removeTrackList()

    fun loadTrackData(): Track
}