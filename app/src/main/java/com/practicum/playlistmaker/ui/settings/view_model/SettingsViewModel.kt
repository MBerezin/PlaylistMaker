package com.practicum.playlistmaker.ui.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var darkTheme = false
    private val themeStateLiveData = MutableLiveData(darkTheme)

    init {
        darkTheme = settingsInteractor.getThemeSettings().darkTheme
        themeStateLiveData.postValue(darkTheme)
    }

    fun observeThemeSwitcherChecked(): LiveData<Boolean> = themeStateLiveData

    fun onClickThemeSwitcher(isChecked: Boolean) {
        themeStateLiveData.postValue(isChecked)
        settingsInteractor.updateThemeSetting(isChecked)
        switchTheme(isChecked)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }
}