package com.practicum.playlistmaker.domain.player.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}