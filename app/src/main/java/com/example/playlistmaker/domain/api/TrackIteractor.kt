package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.modeles.Track

interface TrackIteractor {
    fun searchTrack (expression: String, consumer: TrackConsumer)

    interface TrackConsumer{
        fun consume(foundTreks: List<Track>)
    }
}