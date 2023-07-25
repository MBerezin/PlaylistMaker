package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MediaPlayerApi
import com.practicum.playlistmaker.domain.api.PlayerStateListener
import com.practicum.playlistmaker.domain.model.PlayerState

class MediaPlayerApiImpl(
    private val url: String

): MediaPlayerApi {
    private var listener: PlayerStateListener? = null
    private var mediaPlayer = MediaPlayer()

    init {
        listener?.onStateChanged(PlayerState.STATE_DEFAULT)
    }

    override fun pause() {
        mediaPlayer.pause()
        listener?.onStateChanged(PlayerState.STATE_PAUSED)
    }

    override fun start() {
        mediaPlayer.start()
        listener?.onStateChanged(PlayerState.STATE_PLAYING)
    }

    override fun prepare() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener?.onStateChanged(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            listener?.onStateChanged(PlayerState.STATE_PREPARED)
        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getTrackTimeRemained(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setListener(listener: PlayerStateListener?){
        this.listener = listener
    }
}