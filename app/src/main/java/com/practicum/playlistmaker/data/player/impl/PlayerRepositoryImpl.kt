package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.api.PlayerRepository
import com.practicum.playlistmaker.domain.player.api.PlayerStateListener
import com.practicum.playlistmaker.domain.player.consumer.Consumer
import com.practicum.playlistmaker.domain.player.consumer.ConsumerData
import com.practicum.playlistmaker.domain.player.dao.TrackDAO
import com.practicum.playlistmaker.domain.player.model.PlayerState
import com.practicum.playlistmaker.domain.player.model.Track

class PlayerRepositoryImpl(
    private val trackDAO: TrackDAO,
) : PlayerRepository {

    private var listener: PlayerStateListener? = null
    private var mediaPlayer: MediaPlayer? = null

    init {
        listener?.onStateChanged(PlayerState.STATE_DEFAULT)
    }

    override fun getTrackDetails(consumer: Consumer<Track>) {
        val track = trackDAO.getTrack()

        if (track == null){
            consumer.consume(ConsumerData.Error("Что-то пошло не так, попробуйте еще раз :("))
        } else if(track.previewUrl.isNullOrEmpty()){
            consumer.consume(ConsumerData.Error("Отсутствует предварительный просмотр :("))
        } else {
            consumer.consume(ConsumerData.Data(track))
        }
    }

    override fun playTrack() {
        this.mediaPlayer?.start()
        listener?.onStateChanged(PlayerState.STATE_PLAYING)
    }

    override fun pauseTrack() {
        this.mediaPlayer?.pause()
        listener?.onStateChanged(PlayerState.STATE_PAUSED)
    }

    override fun releasePlayer() {
        this.mediaPlayer?.release()
    }

    override fun getTrackTimeRemained(): Int {
        return this.mediaPlayer?.currentPosition ?: 0
    }

    override fun setListener(listener: PlayerStateListener) {
        this.listener = listener
    }

    override fun clearListener() {
        this.listener = null
    }

    override fun preparePlayer(url: String?) {
        if (url.isNullOrEmpty()) return

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(url)
        mediaPlayer!!.prepareAsync()
        mediaPlayer!!.setOnPreparedListener {
            listener?.onStateChanged(PlayerState.STATE_PREPARED)
        }
        mediaPlayer!!.setOnCompletionListener {
            listener?.onStateChanged(PlayerState.STATE_PREPARED)
        }
    }
}