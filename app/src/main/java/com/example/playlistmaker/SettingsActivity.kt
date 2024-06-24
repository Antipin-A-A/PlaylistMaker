package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
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
            Toast.makeText(this,"isChecked save = $status", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"не сработало isChecked = $status", Toast.LENGTH_LONG).show()
        }

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val shareButton = findViewById<ImageView>(R.id.button_share)
        val arrowButton = findViewById<ImageView>(R.id.button_arrow)
        val supportButton = findViewById<ImageView>(R.id.button_support)
        val switchButton = findViewById<SwitchCompat>(R.id.switch_button)

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                CHECKED = switchButton.isChecked
                Toast.makeText(this,"isChecked1 = ${switchButton.isChecked} status = $status", Toast.LENGTH_SHORT).show()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
               CHECKED = switchButton.isChecked
                Toast.makeText(this,"isChecked2 = ${switchButton.isChecked} status = $status", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("digitex150@yandex.ru"))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.string_email_text))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.string_email_tittle))
            }

            startActivity(shareIntent)
        }
        arrowButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_arrow)))
            startActivity(intent)
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.url_share))
            }
            startActivity(intent)
        }
    }
    companion object {
        const val KEY_STATUS = "KEY_STATUS"
        var CHECKED = false
    }

}