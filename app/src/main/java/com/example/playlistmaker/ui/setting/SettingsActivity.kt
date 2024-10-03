package com.example.playlistmaker.ui.setting


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.domain.AppTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.playlistmaker.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSetting()

    }

    private fun initSetting() = with(binding) {
        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        switchButton.isChecked = sharedPreferences.getBoolean(SWITCH_STATUS, false)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as AppTheme).switchTheme(isChecked)
            sharedPreferences.edit()
                .putBoolean(SWITCH_STATUS, isChecked)
                .apply()
        }
        buttonSupport.setOnClickListener {
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
        buttonArrow.setOnClickListener {
            val url = Uri.parse(getString(R.string.url_arrow))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        buttonShare.setOnClickListener {
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
