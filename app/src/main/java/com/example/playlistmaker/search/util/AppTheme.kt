package com.example.playlistmaker.search.util

import androidx.appcompat.app.AppCompatDelegate


object AppTheme {
    fun switchTheme(isChecked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}