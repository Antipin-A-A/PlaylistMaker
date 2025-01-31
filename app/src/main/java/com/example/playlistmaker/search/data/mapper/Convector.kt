package com.example.playlistmaker.search.data.mapper

import android.net.Uri
import android.os.Environment
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.google.gson.Gson
import java.io.File
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

    fun convectorData(releaseDate: String?): String? {
        val regex = Regex("^(\\d{4})$")
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return if (releaseDate == "null" || releaseDate == "No date") {
            return "No date"
        } else {
            if (regex.matches(releaseDate.toString())) {
                releaseDate
            } else {
                val localDateTime = LocalDateTime.parse(releaseDate, pattern)
                localDateTime.year.toString()
            }
        }
    }

    fun urlVerification(url: String?): String? {
        return if (url == "null"){
            "https://audio-ssl.itunes.apple.com/itunes-assets"
        } else{
            url
        }
    }

    fun fromJsonPlayList(string: String): MutableList<Int?>? {
        val gson= Gson()
      return gson.fromJson(string, mutableListOf<Int?>()::class.java)
    }
    fun toJsonPlayList(string: String): String? {
        val gson = Gson()
        return gson.toJson(string)
    }

}