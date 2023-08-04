package com.practicum.playlistmaker.domain.player.api

import com.practicum.playlistmaker.domain.player.consumer.Consumer
import com.practicum.playlistmaker.domain.player.model.Track

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