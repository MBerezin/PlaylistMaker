package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.NetworkError

interface SearchInteractor {
    fun searchTracks(
        searchText: String,
        success: (ArrayList<Track>) -> Unit,
        empty: (NetworkError) -> Unit,
        error: (NetworkError) -> Unit
    )

    fun openPlayer(track: Track)
    fun readHistoryTracks():ArrayList<Track>
    fun addHistoryTrack(track: Track) : ArrayList<Track>
    fun clearHistoryTracks()

}