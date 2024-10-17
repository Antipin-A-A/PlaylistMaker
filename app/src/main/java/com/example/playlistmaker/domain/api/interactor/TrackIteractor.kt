package com.example.playlistmaker.domain.api.interactor

import com.example.playlistmaker.domain.modeles.Track

interface TrackIteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTreks: List<Track>?, errorMessage: String?)
    }

    fun saveTrack(track: Track)

    fun getSavedTracks(): List<Track>

    fun removeTrackList()
}