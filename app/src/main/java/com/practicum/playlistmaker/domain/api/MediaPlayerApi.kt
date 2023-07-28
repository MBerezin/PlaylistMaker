package com.practicum.playlistmaker.domain.api

interface MediaPlayerApi {
    fun pause()
    fun start()
    fun prepare()
    fun release()
    fun getTrackTimeRemained() : Int
    fun setListener(listener: PlayerStateListener?)
}