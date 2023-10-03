package com.practicum.playlistmaker.di

import android.content.Context
import com.practicum.playlistmaker.data.ExternalNavigator
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.player.db.HardCodeTrackDAO
import com.practicum.playlistmaker.data.search.network.api.ITunesApi
import com.practicum.playlistmaker.data.search.network.api.NetworkClient
import com.practicum.playlistmaker.data.search.network.impl.RetrofitClientImpl
import com.practicum.playlistmaker.data.storage.SharedPrefStorageClient
import com.practicum.playlistmaker.domain.player.dao.TrackDAO
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule  = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ITunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitClientImpl(api = get(), androidContext())
    }

    single {
        androidContext().getSharedPreferences("playlist_maker_preferences", Context.MODE_PRIVATE)
    }

    single<TrackDAO> { HardCodeTrackDAO( sharedPref = get()) }

    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }

    single<StorageClient> { SharedPrefStorageClient(sharedPref = get()) }

}