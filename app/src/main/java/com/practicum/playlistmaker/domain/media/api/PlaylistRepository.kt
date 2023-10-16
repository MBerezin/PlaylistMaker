package com.practicum.playlistmaker.domain.media.api

import com.practicum.playlistmaker.domain.media.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun getPlaylists() : Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, trackId: Int?)

}