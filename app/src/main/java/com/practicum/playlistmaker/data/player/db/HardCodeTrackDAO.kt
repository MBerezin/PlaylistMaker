package com.practicum.playlistmaker.data.player.db

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.player.dao.TrackDAO
import com.practicum.playlistmaker.domain.player.model.Track

class HardCodeTrackDAO(context: Context): TrackDAO {

    private val sharedPref = Creator.getSharedPref(context)

    override fun getTrack(): Track? {
        val json = sharedPref.getString("player_track", null)

        if(json !== null){
            return Gson().fromJson(json, Track::class.java)
        }

        return null
    }
}