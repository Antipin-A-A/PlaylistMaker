package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.Object.CONNECT_OK
import com.example.playlistmaker.search.Object.ERROR_CONNECT
import com.example.playlistmaker.search.Object.ERROR_FILE_NOT_FOUND
import com.example.playlistmaker.search.Object.SERVER_ERROR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val retrofit: RetrofitManager, private val connectedManager: ConnectedManager
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
            if (!connectedManager.isConnected()) {
                return Response().apply { resultCode = ERROR_CONNECT }
            }
            if (dto !is TrackSearchRequest) {
                return Response().apply { resultCode = SERVER_ERROR }
            }
            return withContext(Dispatchers.IO) {
                try {
                    val response = retrofit.iTunesService.searchTrack(dto.expression)
                    response.apply { resultCode = CONNECT_OK }
                } catch (e: Throwable) {
                    Response().apply { resultCode = ERROR_FILE_NOT_FOUND }
                }
            }
    }
}



