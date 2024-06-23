package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val shareButton = findViewById<ImageView>(R.id.button_share)
        val arrowButton = findViewById<ImageView>(R.id.button_arrow)
        val supportButton = findViewById<ImageView>(R.id.button_support)

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("digitex150@yandex.ru"))
                putExtra(Intent.EXTRA_TEXT,getString(R.string.string_email_text))
                putExtra(Intent.EXTRA_SUBJECT,getString(R.string.string_email_tittle))
            }

            startActivity(shareIntent)
        }
        arrowButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_arrow)))
            startActivity(intent)
        }

        shareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND).apply{
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.url_share))
            }
            startActivity(intent)
        }
    }
}