package com.practicum.playlistmaker.data.search.api

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun readHistoryTracks(): ArrayList<Track>
    fun addHistoryTrack(tracks: ArrayList<Track>)
    fun clearHistoryTracks()
    fun searchTracks(
        searchText: String
    ): Flow<SearchStates>
    fun openPlayer(track: Track)
}