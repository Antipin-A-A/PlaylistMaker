package com.example.playlistmaker.data.dto

import android.content.Context
import android.content.Intent
import android.net.Uri

class SupportMessage(private val context: Context) {

   fun support(email: String, message: String, tittle: String) {

        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_TEXT, message)
            putExtra(Intent.EXTRA_SUBJECT, tittle)
        }
        context.startActivity(shareIntent)
    }

    fun arrow(text: String) {
        val url = Uri.parse(text)
        val intent = Intent(Intent.ACTION_VIEW, url)
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
       context.startActivity(intent)
    }

    fun share(message: String){
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
      context.startActivity(intent)
    }

}