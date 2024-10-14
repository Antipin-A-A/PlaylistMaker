package com.example.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager() {
    private val iTunesBaseURL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesService: ITunesApi = retrofit.create(ITunesApi::class.java)
}