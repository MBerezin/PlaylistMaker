package com.practicum.playlistmaker.data.search.api

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.NetworkError

interface SearchRepository {
    fun readHistoryTracks(): ArrayList<Track>
    fun addHistoryTrack(tracks: ArrayList<Track>)
    fun clearHistoryTracks()
    fun searchTracks(
        searchText: String,
        success: (ArrayList<Track>) -> Unit,
        empty: (NetworkError) -> Unit,
        error: (NetworkError) -> Unit
    )
    fun openPlayer(track: Track)
}