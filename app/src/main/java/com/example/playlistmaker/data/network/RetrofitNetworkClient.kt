package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient(private val retrofit: RetrofitManager) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val response = retrofit.iTunesService.searchTrack(dto.expression).execute()

            val dody = response.body() ?: Response()

            return dody.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}