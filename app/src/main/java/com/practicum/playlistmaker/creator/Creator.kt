package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.player.MediaPlayerApiImpl
import com.practicum.playlistmaker.data.player.db.HardCodeTrackDAO
import com.practicum.playlistmaker.data.settings.api.SettingsRepository
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.storage.SharedPrefStorageClient
import com.practicum.playlistmaker.data.ExternalNavigator
import com.practicum.playlistmaker.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.search.network.api.ITunesApi
import com.practicum.playlistmaker.data.search.network.api.NetworkClient
import com.practicum.playlistmaker.data.search.api.SearchRepository
import com.practicum.playlistmaker.data.search.network.impl.RetrofitClientImpl
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.domain.player.api.MediaPlayerApi
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.dao.TrackDAO
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.api.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.ui.main.router.NavigationRouter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Creator {

    private val itunesBaseUrl = "http://itunes.apple.com"

    private const val PLAY_LIST_MAKER_PREFERENCES = "playlist_maker_preferences"

    fun providePlayerInteractor(context: Context): PlayerInteractor {
        return PlayerInteractorImpl(provideTrackDAO(context))
    }

    fun provideMediaPlayerApi(url: String): MediaPlayerApi {
        return MediaPlayerApiImpl(url)
    }

    fun provideSharingInteractor(application: Application): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(application))
    }

    fun provideSearchInteractor(application: Application): SearchInteractor {
        return SearchInteractorImpl(
            provideSearchRepository(application)
        )
    }

    fun getNavigationRouter(activity: AppCompatActivity): NavigationRouter {
        return NavigationRouter(activity)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository = provideSettingsRepository(context))
    }

    fun getSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PLAY_LIST_MAKER_PREFERENCES, MODE_PRIVATE)
    }

    private fun getNetworkClient(): NetworkClient {
        return RetrofitClientImpl(getRetrofit().create(ITunesApi::class.java))
    }

    private fun provideTrackDAO(context: Context): TrackDAO {
        return HardCodeTrackDAO(context)
    }

    private fun getExternalNavigator(application: Application): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    private fun provideSearchRepository(application: Application): SearchRepository {
        return SearchRepositoryImpl(
            getSharedPrefStorageClient(application),
            getExternalNavigator(application),
            getNetworkClient()
        )
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSharedPrefStorageClient(context))
    }

    private fun getSharedPrefStorageClient(context: Context): StorageClient {
        return SharedPrefStorageClient(getSharedPref(context))
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}