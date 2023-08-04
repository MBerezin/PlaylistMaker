package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator

const val THEME_SWITCHER_KEY = "key_for_switch_theme"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = Creator.getSharedPref(this)
        darkTheme = sharedPrefs.getBoolean(THEME_SWITCHER_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}