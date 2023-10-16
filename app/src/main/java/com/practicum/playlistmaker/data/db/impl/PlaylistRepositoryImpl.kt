package com.practicum.playlistmaker.data.db.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.data.converters.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.media.api.PlaylistRepository
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.dao.TrackDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val gson: Gson,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor,
    private val trackDAO: TrackDAO,
): PlaylistRepository {
    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(playlists.map {
            playlist -> playlistDbConvertor.map(playlist)
        })
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, trackId: Int?) {
        val typeTokenArrayList = object : TypeToken<ArrayList<Int>>() {}.type
        val playlistTrackIds = gson.fromJson<ArrayList<Int>>(playlist.tracksList, typeTokenArrayList) ?: arrayListOf()

        if (trackId != null) {
            playlistTrackIds.add(trackId)
            val track = trackDAO.getTrack()
            if(track != null){
                appDatabase.playlistTrackDao().insertTrack(playlistTrackDbConvertor.map(track))
                playlist.tracksList = gson.toJson(playlistTrackIds)
                playlist.size = +1
                appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
            }

        }
    }
}