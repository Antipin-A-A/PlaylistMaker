package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    private var status = CHECKED
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState != null) {
            status = savedInstanceState.getBoolean(
                KEY_STATUS,
                CHECKED
            )
        }

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
                CHECKED = switchButton.isChecked
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                CHECKED = switchButton.isChecked
            }
        }
        supportButton.setOnClickListener {
            val message = getString(R.string.string_email_text)
            val tittle = getString(R.string.string_email_tittle)
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("digitex150@yandex.ru"))
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

    companion object {
        const val KEY_STATUS = "KEY_STATUS"
        var CHECKED = false
        const val KEY_SWITCH = "KEY_SWITCH"
    }

}
