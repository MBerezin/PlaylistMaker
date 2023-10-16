package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.api.PlaylistRepository
import com.practicum.playlistmaker.domain.media.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlaylistInteractor {
    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, trackId: Int?) {
        playlistRepository.addTrackToPlaylist(playlist, trackId)
    }


}