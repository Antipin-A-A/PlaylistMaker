package com.example.playlistmaker.sharing.domain.api.reposytory

import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingRepository {
    fun getShareAppLink(): String
    fun getSupportEmailData(): EmailData
    fun getTermsLink(): String
}