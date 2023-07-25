package com.practicum.playlistmaker.presentation.presenter.player

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.consumer.Consumer
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.model.PlayerState
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.mapper.TrackMapper

class PlayerPresenter(
    private val view: PlayerView,
    private val context: Context,
) {

    private val playerInteractor = Creator.providePlayerInteractor(context)
    private var playerState : PlayerState  = PlayerState.STATE_DEFAULT

    private val handler = Handler(Looper.getMainLooper())
    private var currentConsumeRunnable: Runnable? = null

    fun loadTrack(){
        playerInteractor.getTrackDetails(
            consumer = object : Consumer<Track>{
                override fun consume(data: ConsumerData<Track>) {
                    handler.removeCallbacksSafe(currentConsumeRunnable)

                    val consumeRunnable = getConsumeRunnable(data)
                    currentConsumeRunnable = consumeRunnable

                    handler.post(consumeRunnable)
                }

            }
        )
    }

    private fun getConsumeRunnable(data: ConsumerData<Track>): Runnable {
        return Runnable {
            when (data) {
                is ConsumerData.Error -> {
                    //view.showError(data.message)
                }

                is ConsumerData.Data -> {
                    playerInteractor.setListener { state ->
                        when (state) {
                            PlayerState.STATE_PLAYING -> startPlayer()
                            PlayerState.STATE_PREPARED -> preparePlayer()
                            PlayerState.STATE_PAUSED -> pausePlayer()
                            PlayerState.STATE_DEFAULT -> initPlayer()
                        }
                    }
                    val trackInfo = TrackMapper.map(data.value)
                    view.showTrack(trackInfo)
                }
            }
        }
    }

    fun onDestroy() {
        handler.removeCallbacksSafe(currentConsumeRunnable)
        playerInteractor.releasePlayer()
    }

    private fun Handler.removeCallbacksSafe(r: Runnable?) {
        r?.let {
            removeCallbacks(r)
        }
    }

    fun playbackControl() {

        when (playerState) {
            PlayerState.STATE_PLAYING -> playerInteractor.pauseTrack()
            PlayerState.STATE_PREPARED,
            PlayerState.STATE_PAUSED,
            -> playerInteractor.playTrack()

            PlayerState.STATE_DEFAULT -> initPlayerState()
        }
    }

    private fun initPlayer(){
        playerInteractor.clearListener()
    }

    private fun startPlayer() {
        view?.startPlayer()
        playerState = PlayerState.STATE_PLAYING

    }

    fun startPreparePlayer() {
        playerInteractor.preparePlayer()
    }

    private fun preparePlayer() {
        view?.preparePlayer()
        playerState = PlayerState.STATE_PREPARED
    }

    private fun initPlayerState(){
        playerState = PlayerState.STATE_DEFAULT
    }

    private fun pausePlayer() {
        view?.pausePlayer()
        playerState = PlayerState.STATE_PAUSED
    }

    fun getTrackTimeRemained(): Int{
        return playerInteractor.getTrackTimeRemained()
    }

}