package com.practicum.playlistmaker.ui.media.model

import com.practicum.playlistmaker.domain.media.model.Playlist

sealed class ViewModelPlaylistsState(){
    object ListIsEmpty : ViewModelPlaylistsState()

    data class Success(
        val playlists: List<Playlist>,
    ) : ViewModelPlaylistsState()
}
