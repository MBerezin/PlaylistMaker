package com.practicum.playlistmaker.presentation.presenter.player

import com.practicum.playlistmaker.presentation.model.TrackInfo

interface PlayerView {
    fun showTrack(trackInfo: TrackInfo)
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer()
    fun setTrackCompleted()
}