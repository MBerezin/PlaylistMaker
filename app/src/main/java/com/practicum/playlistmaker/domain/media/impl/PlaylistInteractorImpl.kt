package com.practicum.playlistmaker.domain.media.impl

import android.net.Uri
import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.api.PlaylistRepository
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
): PlaylistInteractor {
    override suspend fun saveImageToPrivateStorage(
        uri: Uri,
        folderName: String,
        fileNamePartly: String
    ): Flow<String> {
        return playlistRepository.saveImageToPrivateStorage(uri, folderName, fileNamePartly)
    }

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

    override suspend fun getTracksByPlaylistId(id: Int): Flow<List<Track>> {
        return playlistRepository.getTracksByPlaylistId(id)
    }

    override suspend fun getPlaylist(id: Int): Flow<Playlist> {
        return playlistRepository.getPlaylist(id)
    }

    override fun getPlaytimeFromTracks(tracks: List<Track>): Int {
        var playtime = 0
        if (tracks.size > 0){
            for (track in tracks){
                playtime += track.trackTimeMillis.toInt()
            }
        }
        return playtime
    }

    override suspend fun deleteTrackFromPlaylist(playlist: Playlist, trackId: Int) {
        playlistRepository.deleteTrackFromPlaylist(playlist, trackId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
    }


}