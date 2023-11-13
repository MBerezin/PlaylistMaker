package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.media.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.ui.media.view_model.FavoriteTrackViewModel
import com.practicum.playlistmaker.ui.media.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule  = module {
    viewModel {
        PlayerViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        FavoriteTrackViewModel(get(), get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        PlaylistViewModel(get(), get(), get())
    }

    viewModel {
        EditPlaylistViewModel(get())
    }
}