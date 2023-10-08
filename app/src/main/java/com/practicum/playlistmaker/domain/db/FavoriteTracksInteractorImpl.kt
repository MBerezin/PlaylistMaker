package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository,
) : FavoriteTracksInteractor {
    override suspend fun insertTrack(track: Track) {
        favoriteTracksRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTracksRepository.deleteTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getTracks()
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return favoriteTracksRepository.getTrackIds()
    }


}