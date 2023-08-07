package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.player.api.PlayerRepository
import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.api.SearchRepository
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.api.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import org.koin.dsl.module

val repositoryModule  = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(storageClient = get()) }

    single<SearchRepository> {
        SearchRepositoryImpl(storageClient = get(), externalNavigator = get(), networkClient = get()) }

    single<PlayerRepository> {
        PlayerRepositoryImpl(trackDAO = get()) }

}