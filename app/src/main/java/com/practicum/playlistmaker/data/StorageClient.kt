package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface StorageClient {
    fun saveTheme(themeSettings: ThemeSettings)
    fun getTheme(): ThemeSettings
    fun readHistoryTracks(): ArrayList<Track>
    fun addHistoryTrack(tracks: ArrayList<Track>)
    fun clearHistoryTracks()
    fun openPlayer(track: Track)
}