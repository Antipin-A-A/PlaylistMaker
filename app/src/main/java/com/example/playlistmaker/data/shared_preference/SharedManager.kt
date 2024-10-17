package com.example.playlistmaker.data.shared_preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.example.playlistmaker.data.model.TrackDto
import com.example.playlistmaker.data.dto.ItemsTrack
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val FACTS_LIST_KEY = "FACTS_LIST_KEY"
const val SWITCH_STATUS = "SWITCH_STATUS"

class SharedManager(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)

    fun saveTrackList(track: TrackDto) {
        val savedTrackList = getTrackDtoList().toMutableList()
        val itemsTrack = ItemsTrack()
        itemsTrack.itemsListTrack(savedTrackList, track)
        val createJsonFromFactsList = Gson().toJson(savedTrackList)
        sharedPreferences.edit { putString(FACTS_LIST_KEY, createJsonFromFactsList) }
    }

    fun getTrackDtoList(): List<TrackDto> {
        val json = sharedPreferences.getString(FACTS_LIST_KEY, null)
        return if (json != null) {
            val itemType = object : TypeToken<ArrayList<TrackDto>>() {}.type
            Gson().fromJson<ArrayList<TrackDto>>(json, itemType)
        } else {
            emptyList()
        }
    }

    fun removeTrackList() {
        sharedPreferences.edit { remove(FACTS_LIST_KEY) }
    }

    fun putSwitchStatus(isChecked: Boolean) {
        sharedPreferences.edit { putBoolean(SWITCH_STATUS, isChecked) }

    }

    fun getSwitchStatus(): Boolean {
        return sharedPreferences.getBoolean(SWITCH_STATUS, false)
    }

}
