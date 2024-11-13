package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val retrofit: RetrofitManager, private val connectedManager: ConnectedManager
) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        try {
            if (!connectedManager.isConnected()) {
                return Response().apply { resultCode = -1 }
            }
            if (dto is TrackSearchRequest) {
                val response = retrofit.iTunesService.searchTrack(dto.expression).execute()
                val dody = response.body() ?: Response()
                return dody.apply { resultCode = response.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        } catch (e: IOException) {
            return Response().apply { resultCode = 400 }
        } catch (e: RuntimeException) {
            return Response().apply { resultCode = 400 }
        } catch (e: Exception) {
            return Response().apply { resultCode = 400 }
        }
    }
}



