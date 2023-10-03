package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.ExternalNavigator
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.data.search.network.api.NetworkClient
import com.practicum.playlistmaker.data.search.api.SearchRepository
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val storageClient : StorageClient,
    private val externalNavigator : ExternalNavigator,
    private val networkClient: NetworkClient
) : SearchRepository {
    override fun readHistoryTracks() : ArrayList<Track>{
        return storageClient.readHistoryTracks()
    }

    override fun addHistoryTrack(tracks: ArrayList<Track>) {
        storageClient.addHistoryTrack(tracks)
    }

    override fun clearHistoryTracks() {
        storageClient.clearHistoryTracks()
    }

    override  fun searchTracks(
        searchText: String
    ): Flow<SearchStates> = flow {
        emit(networkClient.searchTracks(
            searchText = searchText
        ))
    }

    override fun openPlayer(track: Track) {
        storageClient.openPlayer(track)
        externalNavigator.openPlayer()
    }
}