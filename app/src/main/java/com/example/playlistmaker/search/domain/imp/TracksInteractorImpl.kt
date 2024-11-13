package com.example.playlistmaker.search.domain.imp


import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.data.network.Resource

class TracksInteractorImpl(
    private val repository: TrackRepository,
    private val repositoryStorage: TrackStorageRepository
) : TrackIteractor {

    override fun searchTrack(expression: String, consumer: TrackIteractor.TrackConsumer) {
        val thread = Thread {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
        thread.start()
    }

    override fun saveTrackList(track: Track) {
        repositoryStorage.saveTrackList(track)
    }

    override fun saveTrack(track: Track) {
        repositoryStorage.saveTrack(track)
    }

    override fun loadTracksList(): List<Track> {
        return repositoryStorage.loadTracksList()
    }

    override fun removeTrackList() {
        repositoryStorage.removeTrack()
    }

    override fun loadTrackData(): Track? {
        return repositoryStorage.loadTrackData()
    }

}