package com.practicum.playlistmaker.ui.player.model

import com.practicum.playlistmaker.domain.media.model.Playlist

sealed class ViewModelPlaylistState{
    object ListIsEmpty : ViewModelPlaylistState()

    data class TrackAdded(
        val playlist: Playlist,
    ) : ViewModelPlaylistState()

    data class TrackExistsByPlaylist(
        val playlist: Playlist,
    ) : ViewModelPlaylistState()

    data class Success(
        val playlist: List<Playlist>,
    ) : ViewModelPlaylistState()
}
