package com.practicum.playlistmaker.ui.player.model

sealed class ViewModelFavoriteState{
    object FavoriteTrack: ViewModelFavoriteState()

    object NotFavoriteTrack: ViewModelFavoriteState()
}
