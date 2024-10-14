package com.example.playlistmaker.domain.impl


import com.example.playlistmaker.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.domain.modeles.Track

class TracksInteractorImpl(
    private val repository: TrackRepository,
    private val repositoryStorage: TrackStorageRepository
) : TrackIteractor {

    override fun searchTrack(expression: String, consumer: TrackIteractor.TrackConsumer) {
        val thread = Thread {
            consumer.consume(repository.searchTracks(expression))
        }
        thread.start()
    }

    override fun saveTrack(track: Track) {
        repositoryStorage.saveTrack(track)
    }

    override fun getSavedTracks(): List<Track> {
        return repositoryStorage.getSavedTracks()
    }

    override fun removeTrackList() {
        repositoryStorage.removeTrack()
    }

}