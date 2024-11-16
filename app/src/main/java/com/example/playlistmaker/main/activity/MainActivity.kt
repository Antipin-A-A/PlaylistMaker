package com.example.playlistmaker.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.player.ui.activity.MusicActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() = with(binding) {
        val intent = this@MainActivity
        buttonSearch.setOnClickListener {
            val intentSearch = Intent(intent, SearchActivity::class.java)
            startActivity(intentSearch)
        }
        buttonMusic.setOnClickListener {
            val intentMusic = Intent(intent, MusicActivity::class.java)
            startActivity(intentMusic)
        }
        buttonSetting.setOnClickListener {
            val intentSetting = Intent(intent, SettingsActivity::class.java)
            startActivity(intentSetting)
        }

    }
}