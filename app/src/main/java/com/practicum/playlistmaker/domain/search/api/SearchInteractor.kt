package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun searchTracks(
        searchText: String
    ): Flow<SearchStates>

    fun openPlayer(track: Track)
    fun readHistoryTracks():ArrayList<Track>
    fun addHistoryTrack(track: Track) : ArrayList<Track>
    fun clearHistoryTracks()

}