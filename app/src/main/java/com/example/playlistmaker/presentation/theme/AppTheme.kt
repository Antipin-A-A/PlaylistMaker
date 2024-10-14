package com.example.playlistmaker.presentation.theme

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