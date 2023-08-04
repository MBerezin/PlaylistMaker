package com.practicum.playlistmaker.domain.search.models

sealed class NetworkError(){
    class ServerError() : NetworkError()

    class EmptyError() : NetworkError()
}
