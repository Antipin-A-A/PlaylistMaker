package com.example.playlistmaker.search.domain.imp


import com.example.playlistmaker.search.data.network.Resource
import com.example.playlistmaker.search.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.search.domain.api.reposirory.TrackRepository
import com.example.playlistmaker.search.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.search.domain.modeles.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TrackRepository,
    private val repositoryStorage: TrackStorageRepository
) : TrackIteractor {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> { Pair(result.data, null)  }
                is Resource.Error -> { Pair(null, result.message) }
            }
        }
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

    override fun loadTrackData(): Track {
        return repositoryStorage.loadTrackData()
    }

}