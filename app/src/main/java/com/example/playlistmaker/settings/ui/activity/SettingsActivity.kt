package com.example.playlistmaker.settings.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSetting()
    }

    private fun initSetting() = with(binding) {

        viewModel.state.observe(this@SettingsActivity) {
            switchButton.isChecked = it
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        switchButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
        buttonSupport.setOnClickListener {
            viewModel.sendSupport()
        }

        buttonArrow.setOnClickListener {
            viewModel.openTerms()
        }

        buttonShare.setOnClickListener {
            viewModel.shareAppLink()
        }
    }

}
