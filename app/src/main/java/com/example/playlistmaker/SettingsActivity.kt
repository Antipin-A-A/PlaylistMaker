package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<TextView>(R.id.button_back)
        val shareButton = findViewById<ImageView>(R.id.button_share)
        val arrowButton = findViewById<ImageView>(R.id.button_arrow)
        val supportButton = findViewById<ImageView>(R.id.button_support)
        val switchButton = findViewById<SwitchCompat>(R.id.switch_button)

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        supportButton.setOnClickListener {
            val message = getString(R.string.string_email_text)
            val tittle = getString(R.string.string_email_tittle)
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_adress_user)))
                putExtra(Intent.EXTRA_TEXT, message)
                putExtra(Intent.EXTRA_SUBJECT, tittle)
            }
            startActivity(shareIntent)
        }
        arrowButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.url_arrow))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        shareButton.setOnClickListener {
            val message = getString(R.string.url_share)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(intent)
        }
    }

}
