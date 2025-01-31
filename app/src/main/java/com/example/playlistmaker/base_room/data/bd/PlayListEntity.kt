package com.example.playlistmaker.base_room.data.bd

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.base_room.data.convector.Converters

@Entity(tableName = "playlist")
@TypeConverters(Converters::class)
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val listName: String?,
    val description: String?,
    val urlImage: String?,
    val listTracksId: List<Int?>?,
    val countTracks: Int?,
)