package com.example.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName : String,
    val artistName : String,
    val trackTimeMillis : String,
    val artworkUrl100 : String,
    val trackId : String,
    val collectionName : String,
    val releaseDate : String,
    val primaryGenreName: String,
    val country: String
){
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun convectorTime(): String? = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis.toLong())

}
