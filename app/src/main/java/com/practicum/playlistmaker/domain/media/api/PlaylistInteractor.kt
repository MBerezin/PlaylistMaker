package com.practicum.playlistmaker.domain.media.api

import android.net.Uri
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun saveImageToPrivateStorage(uri: Uri, folderName: String, fileNamePartly: String): Flow<String>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun getPlaylists() : Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, trackId: Int?)
    suspend fun getTracksByPlaylistId(id: Int): Flow<List<Track>>
    suspend fun getPlaylist(id: Int): Flow<Playlist>
    fun getPlaytimeFromTracks(tracks: List<Track>): Int
    suspend fun deleteTrackFromPlaylist(playlist: Playlist, trackId: Int)
    suspend fun deletePlaylist(playlistId: Int)
}