package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.model.TrackDto
import com.example.playlistmaker.domain.modeles.Track


fun TrackDto.toDomainModel(): Track {
    return Track(
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = Convector.convectorTime(this.trackTimeMillis).toString(),
        artworkUrl100 = this.artworkUrl100,
        trackId = this.trackId,
        collectionName = this.collectionName,
        releaseDate = Convector.convectorData(this.releaseDate).toString(),
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
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