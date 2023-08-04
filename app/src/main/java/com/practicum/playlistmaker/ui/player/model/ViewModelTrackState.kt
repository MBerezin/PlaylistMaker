package com.practicum.playlistmaker.ui.player.model

sealed class ViewModelTrackState{
    data class ShowError(
        val message: String
    ) : ViewModelTrackState()
    data class TrackTimeRemain(
        val time: Int
    ) : ViewModelTrackState()
    data class ShowTrack(
        val trackInfo: TrackInfo
    ) : ViewModelTrackState()
}
