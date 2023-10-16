package com.practicum.playlistmaker.ui.media.model

sealed class ViewModelNewPlaylistState{
    object SaveSuccess : ViewModelNewPlaylistState()
    object SaveError : ViewModelNewPlaylistState()
}
