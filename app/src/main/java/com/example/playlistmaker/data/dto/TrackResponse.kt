package com.example.playlistmaker.data.dto

import com.example.playlistmaker.data.model.TrackDto

data class TrackResponse(val results: List<TrackDto>) : Response()
