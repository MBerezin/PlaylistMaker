package com.practicum.playlistmaker.domain.search.models

import com.practicum.playlistmaker.domain.player.model.Track

sealed class SearchStates(){
    class Success(val data: ArrayList<Track>): SearchStates()
    class EmptyError : SearchStates()
    class ServerError : SearchStates()
    class NoConnection : SearchStates()
}
