package com.example.playlistmaker.base_room.data.convector

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListToString(list: List<Int?>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromStringToList(data: String?): List<Int>? {
        return if (data == ""){
            emptyList()
        }else{
            data?.split(",")?.map { it.toInt() }
        }
    }
}