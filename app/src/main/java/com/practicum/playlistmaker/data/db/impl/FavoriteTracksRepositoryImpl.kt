package com.practicum.playlistmaker.data.db.impl

import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.db.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTracksRepository {
    override suspend fun insertTrack(track: Track) {
        appDatabase
            .trackDao()
            .insertTrack(
                trackDbConvertor.map(track)
            )
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase
            .trackDao()
            .deleteTrack(
                trackDbConvertor.map(track)
            )
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase
            .trackDao()
            .getTracks()
        emit(converterToTrack(tracks))
    }

    override fun getTrackIds(): Flow<List<Int>> = flow {
        val ids = appDatabase
            .trackDao()
            .getTrackIds()
        emit(ids)
    }

    private fun converterToTrack(tracksEntity: List<TrackEntity>): List<Track> {
        return tracksEntity.map { trackEntity -> trackDbConvertor.map(trackEntity) }
    }
}