package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.data.model.TrackDto

class ItemsTrack() {
    fun itemsListTrack(items: MutableList<TrackDto>, it: TrackDto) {
        if (!items.contains(it)) {
            if (items.size < 10) {
                items.add(it)
            } else {
                items.removeLast()
                items.add(0, it)
            }
        } else {
            items.remove(it)
            items.add(0, it)
        }
    }
}