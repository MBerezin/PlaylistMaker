package com.practicum.playlistmaker.ui.media.model

import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.model.Track

sealed class ViewModelPlaylistState(){
    data class TracksSuccessRead(
        val tracks: List<Track>,
        val trackCount: Int,
        val tracksPlaytime: Int,
    ) : ViewModelPlaylistState()

    data class PlaylistSuccessRead(
        val playlist: Playlist,
    ) : ViewModelPlaylistState()

    object PlaylistSuccessTrackDelete : ViewModelPlaylistState()
    object PlaylistNoTracks : ViewModelPlaylistState()
    object PlaylistDeleted : ViewModelPlaylistState()
}
