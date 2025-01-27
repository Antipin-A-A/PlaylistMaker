package com.example.playlistmaker.playlist.domain.model

import android.net.Uri

data class PlayList(
    var id: Int = 0,
    val listName: String?,
    val description: String?,
    val urlImage: Uri?,
    val listTracksId: List<Int?>?,
    val countTracks: Int?,
)