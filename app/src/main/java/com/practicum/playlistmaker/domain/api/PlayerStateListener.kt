package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.model.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}

