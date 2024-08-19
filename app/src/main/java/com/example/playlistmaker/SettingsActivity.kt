package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<Toolbar>(R.id.toolbar)
        val shareButton = findViewById<ImageView>(R.id.button_share)
        val arrowButton = findViewById<ImageView>(R.id.button_arrow)
        val supportButton = findViewById<ImageView>(R.id.button_support)
        val switchButton = findViewById<SwitchCompat>(R.id.switch_button)

        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        switchButton.isChecked = sharedPreferences.getBoolean(SWITCH_STATUS, false)

        buttonBack.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        switchButton.setOnCheckedChangeListener() { buttonView, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            sharedPreferences.edit()
                .putBoolean(SWITCH_STATUS,isChecked)
                .apply()
        }
        supportButton.setOnClickListener {
            val message = getString(R.string.string_email_text)
            val tittle = getString(R.string.string_email_tittle)
            val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_address_user)))
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
        const val SWITCH_STATUS = "SWITCH_STATUS"
    }

}
