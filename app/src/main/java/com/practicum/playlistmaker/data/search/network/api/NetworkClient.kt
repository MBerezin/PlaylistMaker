package com.practicum.playlistmaker.data.search.network.api

import com.practicum.playlistmaker.domain.search.models.SearchStates

interface NetworkClient {
    suspend fun searchTracks(
        searchText: String
    ): SearchStates
}