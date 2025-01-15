package com.example.playlistmaker.base_room.data.convector

import com.example.playlistmaker.base_room.data.bd.TrackEntity
import com.example.playlistmaker.search.domain.modeles.Track

class TrackDbConvertor {

    fun mapToData(track: Track): TrackEntity {
        return TrackEntity(
            track.id,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
          //  true
        )
    }

    fun mapToDomain(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.id,
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
        )
    }

}