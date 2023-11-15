package com.practicum.playlistmaker.ui.media.model

import com.practicum.playlistmaker.domain.media.model.Playlist

sealed class ViewModelNewPlaylistState{
    object SaveSuccess : ViewModelNewPlaylistState()
    object SaveError : ViewModelNewPlaylistState()

    data class ImageSaved(
        val uri: String,
    ) : ViewModelNewPlaylistState()

    data class PlaylistSuccessRead(
        val playlist: Playlist,
    ) : ViewModelNewPlaylistState()
}
