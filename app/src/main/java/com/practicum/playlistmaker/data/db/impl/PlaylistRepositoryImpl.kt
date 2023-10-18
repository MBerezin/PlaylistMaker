package com.practicum.playlistmaker.data.db.impl

import android.net.Uri
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
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val gson: Gson,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor,
    private val trackDAO: TrackDAO,
    private val context: Context,
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
                ++playlist.size
                appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
            }

        }
    }

    override suspend fun saveImageToPrivateStorage(
        uri: Uri,
        folderName: String,
        fileNamePartly: String
    ): Flow<String> = flow{

        val filePath = ContextWrapper(context.applicationContext).getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (filePath!!.exists()) {
            filePath.mkdirs()
        }

        val file = File(
            filePath,
            fileNamePartly + UUID.randomUUID()
                .toString() + ".jpg"
        )

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        emit(file.absolutePath)

    }.flowOn(Dispatchers.IO)

}