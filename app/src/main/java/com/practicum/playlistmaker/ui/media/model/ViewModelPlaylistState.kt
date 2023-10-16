package com.practicum.playlistmaker.ui.media.model

import com.practicum.playlistmaker.domain.media.model.Playlist

sealed class ViewModelPlaylistState(){
    object ListIsEmpty : ViewModelPlaylistState()

    data class Success(
        val playlists: List<Playlist>,
    ) : ViewModelPlaylistState()
}
