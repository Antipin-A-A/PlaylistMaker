package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient(private val retrofit: RetrofitManager, private val connectedManager: ConnectedManager) :
    NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!connectedManager.isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        val response = retrofit.iTunesService.searchTrack(dto.expression).execute()
        val dody = response.body()
        return dody?.apply { resultCode = response.code() } ?: Response().apply { resultCode = 400 }
    }

}


