package com.example.playlistmaker.search.data.shared_preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.data.model.TrackDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val FACTS_LIST_KEY = "FACTS_LIST_KEY"
const val SWITCH_STATUS = "SWITCH_STATUS"
const val NEW_FACT_TRACK_KEY = "NEW_FACT_TRACK_KEY"

class SharedManager(private val sharedPreferences: SharedPreferences) {

    fun saveTrackList(track: TrackDto) {
        val savedTrackList = getTrackDtoList().toMutableList()
        itemsListTrack(savedTrackList, track)
        val createJsonFromFactsList = Gson().toJson(savedTrackList)
        sharedPreferences.edit { putString(FACTS_LIST_KEY, createJsonFromFactsList) }
    }

    fun saveTrack(track: TrackDto) {
        val createJsonFromFact = Gson().toJson(track)
        sharedPreferences.edit { putString(NEW_FACT_TRACK_KEY, createJsonFromFact) }
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

    fun getTrackDto(): TrackDto? {
        val json = sharedPreferences.getString(NEW_FACT_TRACK_KEY, null)
        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, TrackDto::class.java)
        } else {
            null
        }

    }

}

private fun itemsListTrack(items: MutableList<TrackDto>, it: TrackDto) {
    if (!items.contains(it)) {
        if (items.size < 10) {
            items.add(0, it)
        } else {
            items.removeLast()
            items.add(0, it)
        }
    } else {
        items.remove(it)
        items.add(0, it)
    }
}
