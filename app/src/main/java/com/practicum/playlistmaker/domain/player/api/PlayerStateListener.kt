package com.practicum.playlistmaker.domain.player.api

import com.practicum.playlistmaker.domain.player.model.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}

