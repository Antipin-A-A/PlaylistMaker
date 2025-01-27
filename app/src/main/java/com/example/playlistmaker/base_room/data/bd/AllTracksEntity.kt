package com.example.playlistmaker.base_room.data.bd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_tracks_table")
data class AllTracksEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trackId: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    //  var isFavorite: Boolean = false
)