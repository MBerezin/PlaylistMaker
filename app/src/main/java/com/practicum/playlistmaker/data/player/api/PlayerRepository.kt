package com.practicum.playlistmaker.data.player.api

import com.practicum.playlistmaker.domain.player.api.PlayerStateListener
import com.practicum.playlistmaker.domain.player.consumer.Consumer
import com.practicum.playlistmaker.domain.player.model.Track

interface PlayerRepository {
    fun getTrackDetails(
        consumer: Consumer<Track>
    ) : Track?

    fun playTrack()
    fun pauseTrack()
    fun releasePlayer()
    fun getTrackTimeRemained(): Int
    fun setListener(listener: PlayerStateListener)
    fun clearListener()
    fun preparePlayer(url: String?)
}