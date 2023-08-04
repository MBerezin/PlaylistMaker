package com.practicum.playlistmaker.data.search.network.api

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.NetworkError

interface NetworkClient {
    fun searchTracks(
        searchText: String,
        empty: (NetworkError) -> Unit,
        success: (ArrayList<Track>) -> Unit,
        error:  (NetworkError) -> Unit
    )
}