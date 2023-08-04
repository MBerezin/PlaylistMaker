package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.data.settings.api.SettingsRepository
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor{
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(isChecked: Boolean) {
        settingsRepository.updateThemeSetting(ThemeSettings(isChecked))
    }
}