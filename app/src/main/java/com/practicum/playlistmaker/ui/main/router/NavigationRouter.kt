package com.practicum.playlistmaker.ui.main.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.ui.media.activity.MediaActivity
import com.practicum.playlistmaker.ui.search.activity.SearchActivity
import com.practicum.playlistmaker.ui.settings.activity.SettingsActivity

class NavigationRouter(
    private val activity: AppCompatActivity
) {

    fun getSearch() {
        val searchIntent = Intent(activity, SearchActivity::class.java)
        activity.startActivity(searchIntent)
    }

    fun getSettings() {
        val settingsIntent = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(settingsIntent)
    }

    fun getMedia() {
        val mediaIntent = Intent(activity, MediaActivity::class.java)
        activity.startActivity(mediaIntent)
    }

}