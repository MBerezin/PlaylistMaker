package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val THEME_SWITCHER_KEY = "key_for_switch_theme"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor = getKoin().get()

        darkTheme = settingsInteractor.getThemeSettings().darkTheme
        switchTheme(darkTheme)
        PermissionRequester.initialize(applicationContext)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}