package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.data.MediaPlayerApiImpl
import com.practicum.playlistmaker.data.db.HardCodeTrackDAO
import com.practicum.playlistmaker.domain.api.MediaPlayerApi
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.dao.TrackDAO
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.presentation.presenter.player.PlayerPresenter
import com.practicum.playlistmaker.presentation.presenter.player.PlayerView


object Creator {

    private fun providePlayerInteractor(context: Context): PlayerInteractor{
        return PlayerInteractorImpl(provideTrackDAO(context))
    }

    fun providePlayerPresenter(view: PlayerView, context: Context): PlayerPresenter {
        return PlayerPresenter(view, providePlayerInteractor(context))
    }

    private fun provideTrackDAO(context: Context): TrackDAO {
        return HardCodeTrackDAO(context)
    }

    fun provideMediaPlayerApi(url: String): MediaPlayerApi{
        return MediaPlayerApiImpl(url)
    }
}