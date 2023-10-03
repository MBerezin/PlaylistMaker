package com.practicum.playlistmaker.data.search.network.impl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.practicum.playlistmaker.data.search.network.api.ITunesApi
import com.practicum.playlistmaker.data.search.network.api.NetworkClient
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.models.SearchStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitClientImpl(
    private val api: ITunesApi,
    private val context: Context
) : NetworkClient{
    override suspend fun searchTracks(
        searchText: String
    ): SearchStates {

        if (!isConnected()){
            return SearchStates.NoConnection()
        }

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

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}