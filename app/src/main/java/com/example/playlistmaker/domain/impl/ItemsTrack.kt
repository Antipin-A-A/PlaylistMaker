package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.modeles.Track


class ItemsTrack() {
    fun itemsListTrack(items : MutableList<Track>, it : Track) {
        if (!items.contains(it)) {
            if (items.size < 10) {
                items.add(it)
            } else {
                items.removeLast()
                items.add(0,it)
            }
        } else {
            items.remove(it)
            items.add(0, it)
        }
    }
}