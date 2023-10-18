package com.practicum.playlistmaker.domain.media.api

import android.net.Uri
import com.practicum.playlistmaker.domain.media.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun saveImageToPrivateStorage(uri: Uri, folderName: String, fileNamePartly: String): Flow<String>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun getPlaylists() : Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, trackId: Int?)

}