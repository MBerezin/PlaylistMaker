package com.practicum.playlistmaker.data.search.network.api

import com.practicum.playlistmaker.data.search.network.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("search")
    suspend fun search(@Query("term", encoded = false) text: String): TracksResponse
}