package com.example.playlistmaker.sharing.domain.imp

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.interact.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.interact.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    val context: Context
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.url_share)
    }

    private fun getSupportEmailData(): EmailData {
        val email = context.getString(R.string.mail_address_user)
        val message = context.getString(R.string.string_email_text)
        val tittle = context.getString(R.string.string_email_tittle)
        return EmailData(email, message, tittle)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.url_arrow)
    }
}