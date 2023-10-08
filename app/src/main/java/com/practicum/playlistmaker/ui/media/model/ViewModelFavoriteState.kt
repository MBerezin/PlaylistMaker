package com.practicum.playlistmaker.ui.media.model

import com.practicum.playlistmaker.domain.player.model.Track

sealed class ViewModelFavoriteState{
    object ListIsEmpty : ViewModelFavoriteState()

    data class Success(
        val tracks: List<Track>,
    ) : ViewModelFavoriteState()
}
