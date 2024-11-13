package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.modeles.Track

fun interface OnItemClickListener {
    fun onItemClick(item : Track)
}