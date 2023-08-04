package com.practicum.playlistmaker.data.settings.impl

import com.practicum.playlistmaker.data.settings.api.SettingsRepository
import com.practicum.playlistmaker.data.StorageClient
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(
    private val storageClient: StorageClient
) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return storageClient.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storageClient.saveTheme(settings)
    }
}