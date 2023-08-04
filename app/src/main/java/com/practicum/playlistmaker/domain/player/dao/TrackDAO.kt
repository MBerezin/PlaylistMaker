package com.practicum.playlistmaker.domain.player.dao

import com.practicum.playlistmaker.domain.player.model.Track

interface TrackDAO {
    fun getTrack(): Track?
}