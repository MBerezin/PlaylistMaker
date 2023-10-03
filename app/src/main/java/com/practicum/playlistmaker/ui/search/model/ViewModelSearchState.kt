package com.practicum.playlistmaker.ui.search.model

import com.practicum.playlistmaker.domain.player.model.Track

sealed class ViewModelSearchState{
    data class SuccessSearchState(
        val tracks: ArrayList<Track>
    ) : ViewModelSearchState()
    object EmptySearchState: ViewModelSearchState()
    object ErrorSearchState: ViewModelSearchState()
    object StartSearchState: ViewModelSearchState()
    object NoConnectionSearchState: ViewModelSearchState()
}
