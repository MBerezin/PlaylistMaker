package com.practicum.playlistmaker.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SharedPrefStorageClient(
    private val sharedPref: SharedPreferences
): StorageClient {

    companion object{
        const val THEME_SWITCHER_KEY = "key_for_switch_theme"
        const val SEARCH_HISTORY_KEY = "key_for_search_history"
        const val PLAYER_TRACK = "player_track"
    }

    override fun saveTheme(themeSettings: ThemeSettings) {
        sharedPref.edit()
            .putBoolean(com.practicum.playlistmaker.THEME_SWITCHER_KEY, themeSettings.darkTheme)
            .apply()
    }

    override fun getTheme(): ThemeSettings {
        return ThemeSettings(darkTheme = sharedPref.getBoolean(THEME_SWITCHER_KEY, false))
    }

    override fun readHistoryTracks(): ArrayList<Track> {
        val tracks = arrayListOf<Track>()
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, null)
        if (json !== null) {
            val tracksFromJson = Gson().fromJson(json, Array<Track>::class.java)
            tracks.addAll(tracksFromJson)
        }

        return tracks
    }

    override fun addHistoryTrack(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPref.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    override fun clearHistoryTracks() {
        sharedPref.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    override fun openPlayer(track: Track) {
        sharedPref.edit().putString(PLAYER_TRACK, Gson().toJson(track)).apply()
    }
}

