package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(
        searchText: String
    ): Flow<SearchStates>

    fun openPlayer(track: Track)
    suspend fun readHistoryTracks():ArrayList<Track>
    suspend fun addHistoryTrack(track: Track) : ArrayList<Track>
    fun clearHistoryTracks()

}