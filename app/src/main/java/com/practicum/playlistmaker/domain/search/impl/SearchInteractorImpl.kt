package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.data.search.api.SearchRepository
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val searchRepository: SearchRepository
) : SearchInteractor{

    override fun searchTracks(
        searchText: String
    ): Flow<SearchStates> {
        return searchRepository.searchTracks(
            searchText = searchText
        )
    }

    override fun openPlayer(track: Track) {
        searchRepository.openPlayer(track)
    }

    override fun readHistoryTracks() : ArrayList<Track> {
        return searchRepository.readHistoryTracks()
    }

    override fun addHistoryTrack(track: Track) : ArrayList<Track> {
        val tracks = searchRepository.readHistoryTracks()

        if (tracks.find { it.trackId === track.trackId } !== null) {
            tracks.remove(track)
            tracks.add(0, track)
        } else {
            if (tracks.size === 10) {
                tracks.removeLast()
                tracks.add(0, track)
            } else {
                tracks.add(0, track)
            }
        }
        searchRepository.addHistoryTrack(tracks)
        return tracks
    }

    override fun clearHistoryTracks() {
        searchRepository.clearHistoryTracks()
    }
}