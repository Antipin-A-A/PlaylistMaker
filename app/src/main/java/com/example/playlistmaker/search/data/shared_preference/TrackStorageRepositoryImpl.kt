package com.example.playlistmaker.search.data.shared_preference

import com.example.playlistmaker.base_room.data.convector.PlayListDbConvector
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.data.mapper.mapToDomain
import com.example.playlistmaker.search.data.mapper.toDataModel
import com.example.playlistmaker.search.data.mapper.toDomainModel
import com.example.playlistmaker.search.domain.api.reposirory.TrackStorageRepository
import com.example.playlistmaker.search.domain.modeles.Track

class TrackStorageRepositoryImpl(private val sharedManager: SharedManager,private val playListDbConvector: PlayListDbConvector) :
    TrackStorageRepository {

    override fun saveTrackList(track: Track) {
        sharedManager.saveTrackList(track.toDataModel())
    }

    override fun saveTrack(track: Track) {
        sharedManager.saveTrack(track.toDataModel())
    }

    override fun loadTracksList(): List<Track> {
        return sharedManager.getTrackDtoList().map { it.toDomainModel() }
    }

    override fun removeTrack() {
        sharedManager.removeTrackList()
    }

    override fun loadTrackData(): Track {
        return sharedManager.getTrackDto().toDomainModel()
    }

  override  fun savePlaylist(playlist: PlayList){
       sharedManager.savePlaylist(playListDbConvector.mapToData(playlist))
    }

  override  fun getCurrentPlaylist(): PlayList {
        return sharedManager.getPlaylist().mapToDomain()
    }
}