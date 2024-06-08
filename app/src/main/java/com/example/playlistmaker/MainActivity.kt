package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonMusic = findViewById<Button>(R.id.button_music)
        val buttonSettings = findViewById<Button>(R.id.button_setting)

        buttonSearch.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }
        buttonMusic.setOnClickListener {
            val intentMusic = Intent(this, MusicActivity::class.java)
            startActivity(intentMusic)
        }
        buttonSettings.setOnClickListener {
            val intentSearch = Intent(this, SettingsActivity::class.java)
            startActivity(intentSearch)
        }
    }
}