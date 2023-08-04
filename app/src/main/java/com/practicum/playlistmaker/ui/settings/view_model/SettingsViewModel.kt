package com.practicum.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.api.SharingInteractor

class SettingsViewModel(
    private val application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {
    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SettingsViewModel(
                    application,
                    Creator.provideSharingInteractor(application),
                    Creator.provideSettingsInteractor(application)
                )
            }
        }
    }

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