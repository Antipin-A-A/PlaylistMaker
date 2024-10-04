package com.example.playlistmaker.data.dto

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val FACTS_LIST_KEY = "FACTS_LIST_KEY"
const val NEW_FACT_KEY = "NEW_FACT_KEY"


class SharedPrefTrack(private val context: Context) : TrackStorage {


    private val sharedPreferences =
        context.getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)


    override fun saveAllTrackList(trackDto: List<TrackDto>) {
        sharedPreferences.edit()
            .putString(FACTS_LIST_KEY, createJsonFromFactsList(trackDto.toMutableList()))
            .apply()
    }

    override fun getTrack(): List<TrackDto> {
        val factTrack = sharedPreferences.getString(FACTS_LIST_KEY, null)
        return if (factTrack != null) {
            createFactsListFromJson(factTrack)
        } else {
            emptyList()
        }
    }

    fun removeAllTrackList() {
        sharedPreferences.edit()
            .remove(FACTS_LIST_KEY)
            .apply()
    }



}

private fun createJsonFromFactsList(facts: MutableList<TrackDto>): String {
    return Gson().toJson(facts)
}

private fun createFactsListFromJson(json: String?): ArrayList<TrackDto> {
    val itemType = object : TypeToken<ArrayList<TrackDto?>?>() {}.type
    val itemList = Gson().fromJson<ArrayList<TrackDto>>(json, itemType)
    return itemList
}