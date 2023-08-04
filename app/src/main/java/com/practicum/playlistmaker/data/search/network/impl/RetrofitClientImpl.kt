package com.practicum.playlistmaker.data.search.network.impl

import com.practicum.playlistmaker.data.search.network.TracksResponse
import com.practicum.playlistmaker.data.search.network.api.ITunesApi
import com.practicum.playlistmaker.data.search.network.api.NetworkClient
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.NetworkError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitClientImpl(
    private val api: ITunesApi
) : NetworkClient{
    override fun searchTracks(
        searchText: String,
        empty: (NetworkError) -> Unit,
        success: (ArrayList<Track>) -> Unit,
        error: (NetworkError) -> Unit
    ) {
        api.search(searchText)
            .enqueue(object : Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            success.invoke(response.body()?.results!! as ArrayList<Track>)
                        } else {
                            empty.invoke(NetworkError.EmptyError())
                        }
                    } else {
                        error.invoke(NetworkError.ServerError())
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    error.invoke(NetworkError.ServerError())
                }
            })
    }
}