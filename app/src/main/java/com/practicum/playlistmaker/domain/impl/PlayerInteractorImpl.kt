package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.creator.Creator.provideMediaPlayerApi
import com.practicum.playlistmaker.domain.api.MediaPlayerApi
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerStateListener
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.dao.TrackDAO
import com.practicum.playlistmaker.domain.model.Track

class PlayerInteractorImpl(
    private val trackDAO: TrackDAO
): PlayerInteractor {

    private lateinit var mediaPlayerApi: MediaPlayerApi

    override fun getTrackDetails(
        consumer: Consumer<Track>
    ) {
        val track = trackDAO.getTrack()

        if (track == null){
            consumer.consume(ConsumerData.Error("Что-то пошло не так, попробуйте еще раз :("))
        } else {
            mediaPlayerApi = provideMediaPlayerApi(track.previewUrl)
            consumer.consume(ConsumerData.Data(track))
        }
    }

    override fun playTrack() {
        mediaPlayerApi.start()
    }

    override fun pauseTrack() {
        mediaPlayerApi.pause()
    }

    override fun releasePlayer() {
        mediaPlayerApi.release()
    }

    override fun getTrackTimeRemained(): Int {
        return mediaPlayerApi.getTrackTimeRemained()
    }

    override fun setListener(listener: PlayerStateListener) {
        mediaPlayerApi.setListener(listener)
    }

    override fun clearListener() {
        mediaPlayerApi.setListener(null)
    }

    override fun preparePlayer() {
        mediaPlayerApi.prepare()
    }
}