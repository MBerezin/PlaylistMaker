package com.practicum.playlistmaker.domain.dao

import com.practicum.playlistmaker.domain.model.Track

interface TrackDAO {
    fun getTrack(): Track?
}