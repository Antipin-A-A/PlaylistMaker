package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.modeles.Track

fun interface OnItemClickListener <T> {
    fun onItemClick(item : T)
}

fun interface OnItemLongClickListener {
    fun onItemLongClick(track: Track)
}