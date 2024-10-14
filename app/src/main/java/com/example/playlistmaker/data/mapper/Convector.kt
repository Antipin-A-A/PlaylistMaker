package com.example.playlistmaker.data.mapper

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Convector {
    fun convectorTime(trackTimeMillis: String): String? {
        val regex = Regex("^([0-5][0-9]|[0-9]):([0-5][0-9])$")
        return if (regex.matches(trackTimeMillis)) {
            trackTimeMillis
        } else {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(trackTimeMillis.toLong()))
        }
    }

    fun convectorData(releaseDate: String): String {
        val regex = Regex("^(\\d{4})$")
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return if (regex.matches(releaseDate)) {
            releaseDate
        } else {
            val localDateTime = LocalDateTime.parse(releaseDate, pattern)
            localDateTime.year.toString()
        }
    }
}