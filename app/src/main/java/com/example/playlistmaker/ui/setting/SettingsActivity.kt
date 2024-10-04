package com.example.playlistmaker.ui.setting


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.domain.AppTheme
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.PRACTICUM_EXAMPLE_PREFERENCES
import com.example.playlistmaker.data.dto.SupportMessage
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

        val supportMessage = SupportMessage(applicationContext)

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
            val email = getString(R.string.mail_address_user)
            val message = getString(R.string.string_email_text)
            val tittle = getString(R.string.string_email_tittle)
            supportMessage.support(email,message,tittle)

        }
        buttonArrow.setOnClickListener {
            val uri = getString(R.string.url_arrow)
            supportMessage.arrow(uri)
        }

        buttonShare.setOnClickListener {
            val message = getString(R.string.url_share)
            supportMessage.share(message)
        }
    }

    companion object {
        const val SWITCH_STATUS = "SWITCH_STATUS"
    }
}
