package com.practicum.playlistmaker.domain.api

interface MediaPlayerApi {
    /*var listener: PlayerStateListener?*/
    fun pause()
    fun start()
    fun prepare()
    fun release()
    fun getTrackTimeRemained() : Int
    fun setListener(listener: PlayerStateListener?)
}