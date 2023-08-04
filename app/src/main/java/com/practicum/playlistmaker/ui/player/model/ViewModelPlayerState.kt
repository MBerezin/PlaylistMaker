package com.practicum.playlistmaker.ui.player.model

sealed class ViewModelPlayerState{
    object Prepare : ViewModelPlayerState()
    object Pause : ViewModelPlayerState()
    object Play : ViewModelPlayerState()
    object Stop : ViewModelPlayerState()
}
