package com.example.playlistmaker.search.data.mapper

import androidx.core.net.toUri
import com.example.playlistmaker.base_room.data.bd.PlayListEntity
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.search.data.model.TrackDto
import com.example.playlistmaker.search.domain.modeles.Track


fun TrackDto.toDomainModel(): Track {
    return Track(
        trackName = this.trackName.toString(),
        artistName = this.artistName.toString(),
        trackTimeMillis = Convector.convectorTime(this.trackTimeMillis.toString()).toString(),
        artworkUrl100 = this.artworkUrl100.toString(),
        trackId = this.trackId.toString(),
        collectionName = this.collectionName.toString(),
        releaseDate = Convector.convectorData(this.releaseDate.toString()),
        primaryGenreName = this.primaryGenreName.toString(),
        country = this.country.toString(),
        previewUrl = Convector.urlVerification(this.previewUrl.toString())
    )
}

fun Track.toDataModel(): TrackDto {
    return TrackDto(
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        trackId = this.trackId,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )

}

fun PlayListEntity.mapToDomain(): PlayList {
    return PlayList(
        this.id,
        this.listName,
        this.description,
        this.urlImage?.toUri(),
        this.listTracksId,
        this.countTracks,
    )
}