package com.example.playlistmaker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val iTunesBaseURL = "https://itunes.apple.com"
const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val NEW_FACT_KEY = "NEW_FACT_KEY"

private val retrofit = Retrofit.Builder()
    .baseUrl(iTunesBaseURL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val iTunesService : ITunesApi = retrofit.create(ITunesApi::class.java)

