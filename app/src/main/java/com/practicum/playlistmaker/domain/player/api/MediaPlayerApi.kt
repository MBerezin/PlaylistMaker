package com.practicum.playlistmaker.domain.player.api

interface MediaPlayerApi {
    fun pause()
    fun start()
    fun prepare()
    fun release()
    fun getTrackTimeRemained() : Int
    fun setListener(listener: PlayerStateListener?)
}