package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val THEME_DAY_NIGHT = "THEME_DAY_NIGHT"

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)

        darkTheme = sharedPreferences.getBoolean(THEME_DAY_NIGHT, false)

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled : Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit()
            .putBoolean(THEME_DAY_NIGHT, darkTheme)
            .apply()
    }

}