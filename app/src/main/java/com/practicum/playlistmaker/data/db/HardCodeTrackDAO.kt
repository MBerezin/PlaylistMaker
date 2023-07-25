package com.practicum.playlistmaker.data.db

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.dao.TrackDAO
import com.practicum.playlistmaker.domain.model.Track

class HardCodeTrackDAO(context: Context): TrackDAO {

    private val sharedPref = context.getSharedPreferences(
        "playlist_maker_preferences",
        AppCompatActivity.MODE_PRIVATE
    )

    override fun getTrack(): Track? {
        val json = sharedPref.getString("player_track", null)

        if(json !== null){
            return Gson().fromJson(json, Track::class.java)
        }

        return null
    }
}