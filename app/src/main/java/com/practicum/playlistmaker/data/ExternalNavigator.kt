package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.model.Track

interface ExternalNavigator {

    fun shareLink()
    fun openLink()
    fun openEmail()
    fun openPlayer()
    fun sharePlaylist(playlist: Playlist, tracks: List<Track>)
}