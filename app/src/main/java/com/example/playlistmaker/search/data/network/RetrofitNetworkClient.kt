package com.example.playlistmaker.search.data.network

import android.util.Log
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val retrofit: RetrofitManager, private val connectedManager: ConnectedManager
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
            if (!connectedManager.isConnected()) {
                Log.i("Log1-RetrofitNetworkClient -1", "${connectedManager.isConnected()}")
                return Response().apply { resultCode = -1 }
            }
            if (dto !is TrackSearchRequest) {
                Log.i("Log2-RetrofitNetworkClient -2", "${dto !is TrackSearchRequest}")
                return Response().apply { resultCode = 400 }
            }
            return withContext(Dispatchers.IO) {
                try {
                    val response = retrofit.iTunesService.searchTrack(dto.expression)
                    Log.i("Log3-RetrofitNetworkClient -3", "$response")
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Log.i("Log4-RetrofitNetworkClient -4", "$e")
                    Response().apply { resultCode = 500 }
                }
            }
    }
}



