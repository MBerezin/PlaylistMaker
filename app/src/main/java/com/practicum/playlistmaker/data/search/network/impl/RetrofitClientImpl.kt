package com.practicum.playlistmaker.data.search.network.impl

import com.practicum.playlistmaker.data.search.network.api.ITunesApi
import com.practicum.playlistmaker.data.search.network.api.NetworkClient
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitClientImpl(
    private val api: ITunesApi
) : NetworkClient{
    override suspend fun searchTracks(
        searchText: String
    ): SearchStates {

        return withContext(Dispatchers.IO){
            try {
                val response = api.search(searchText)
                if(response.resultCount > 0){
                    SearchStates.Success(response.results as ArrayList<Track>)
                } else {
                    SearchStates.EmptyError()
                }
            } catch (e: Throwable){
                SearchStates.ServerError()
            }
        }
    }

}