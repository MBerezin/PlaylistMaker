package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.media.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.impl.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.domain.media.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.api.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule  = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    single<SharingInteractor> { SharingInteractorImpl(externalNavigator = get()) }

    single<PlayerInteractor> { PlayerInteractorImpl(playerRepository = get()) }

    single<SearchInteractor> { SearchInteractorImpl(searchRepository = get()) }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

}