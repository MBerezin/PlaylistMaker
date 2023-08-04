package com.practicum.playlistmaker.data.settings.api

import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}