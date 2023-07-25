package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.data.MediaPlayerApiImpl
import com.practicum.playlistmaker.data.db.HardCodeTrackDAO
import com.practicum.playlistmaker.domain.api.MediaPlayerApi
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.dao.TrackDAO
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl


object Creator {

    fun providePlayerInteractor(context: Context): PlayerInteractor{
        return PlayerInteractorImpl(provideTrackDAO(context))
    }

    private fun provideTrackDAO(context: Context): TrackDAO {
        return HardCodeTrackDAO(context)
    }

    fun provideMediaPlayerApi(url: String): MediaPlayerApi{
        return MediaPlayerApiImpl(url)
    }
}