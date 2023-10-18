package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.converters.PlaylistDbConvertor
import com.practicum.playlistmaker.data.converters.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.db.impl.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.data.db.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.data.player.api.PlayerRepository
import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.api.SearchRepository
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.api.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.media.api.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.media.api.PlaylistRepository
import org.koin.dsl.module

val repositoryModule  = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(storageClient = get()) }

    single<SearchRepository> {
        SearchRepositoryImpl(storageClient = get(), externalNavigator = get(), networkClient = get(), appDatabase = get()) }

    single<PlayerRepository> {
        PlayerRepositoryImpl(trackDAO = get()) }

    factory { TrackDbConvertor() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    factory { PlaylistDbConvertor() }

    factory { PlaylistTrackDbConvertor() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get(), get(), get())
    }

}