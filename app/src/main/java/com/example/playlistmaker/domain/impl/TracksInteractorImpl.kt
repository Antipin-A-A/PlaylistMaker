package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackIteractor
import com.example.playlistmaker.domain.api.TrackRepository

class TracksInteractorImpl(private val repository: TrackRepository) : TrackIteractor {

    override fun searchTrack(expression: String, consumer: TrackIteractor.TrackConsumer) {
        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        t.start()
    }

}