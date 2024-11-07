package com.example.playlistmaker.search.data.dto

import com.example.playlistmaker.search.data.model.TrackDto

data class TrackResponse(val results: List<TrackDto>) : Response()
