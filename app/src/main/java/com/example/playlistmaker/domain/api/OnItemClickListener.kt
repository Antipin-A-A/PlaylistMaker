package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.modeles.Track

fun interface OnItemClickListener {
    fun onItemClick(item : Track)
}