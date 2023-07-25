package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.model.Track

interface PlayerInteractor {

    fun getTrackDetails(consumer: Consumer<Track>)
    fun playTrack()
    fun pauseTrack()
    fun releasePlayer()
    fun getTrackTimeRemained(): Int
    fun setListener(listener: PlayerStateListener)
    fun clearListener()
    fun preparePlayer()
}