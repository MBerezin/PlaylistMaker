package com.practicum.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.consumer.Consumer
import com.practicum.playlistmaker.domain.player.consumer.ConsumerData
import com.practicum.playlistmaker.domain.player.model.PlayerState
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.player.model.ViewModelPlayerState
import com.practicum.playlistmaker.ui.player.model.ViewModelTrackState
import com.practicum.playlistmaker.ui.player.mapper.TrackMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    companion object {

        private const val PROGRESS_TIME_REMAINED_DELAY = 300L

    }

    private val playerStateLiveData = MutableLiveData<ViewModelPlayerState>()
    private val trackStateLiveData = MutableLiveData<ViewModelTrackState>()
    private val handler = Handler(Looper.getMainLooper())
    private var currentConsumeRunnable: Runnable? = null
    private var playerState : PlayerState = PlayerState.STATE_DEFAULT

    private var timerJob: Job? = null
    
    fun observePlayerState(): LiveData<ViewModelPlayerState> = playerStateLiveData
    fun observeTrackState(): LiveData<ViewModelTrackState> = trackStateLiveData

    fun loadTrack(){
        playerInteractor.getTrackDetails(
            consumer = object : Consumer<Track> {
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
                    trackStateLiveData.postValue(ViewModelTrackState.ShowError(data.message))
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

                    trackStateLiveData.postValue(ViewModelTrackState.ShowTrack(trackInfo))
                }
            }
        }
    }

    override fun onCleared() {
        handler.removeCallbacksSafe(currentConsumeRunnable)
        handler.removeCallbacksAndMessages(null)
        playerInteractor.releasePlayer()
        super.onCleared()
    }

    private fun Handler.removeCallbacksSafe(r: Runnable?) {
        r?.let {
            removeCallbacks(r)
        }
    }

    fun playbackControl() {

        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                playerInteractor.pauseTrack()
                timerJob?.cancel()
                playerStateLiveData.postValue(ViewModelPlayerState.Pause)
            }
            PlayerState.STATE_PREPARED,
            PlayerState.STATE_PAUSED,
            -> {
                playerInteractor.playTrack()
                playerStateLiveData.postValue(ViewModelPlayerState.Play)
            }

            PlayerState.STATE_DEFAULT -> initPlayerState()
        }
    }

    private fun initPlayer(){
        playerInteractor.clearListener()
    }

    private fun startPlayer() {
        playerStateLiveData.postValue(ViewModelPlayerState.Play)
        playerState = PlayerState.STATE_PLAYING
        setTrackTimeRemained()
    }

    private fun setTrackTimeRemained(){
        timerJob = viewModelScope.launch {
            while (playerState === PlayerState.STATE_PLAYING){
                delay(PROGRESS_TIME_REMAINED_DELAY)
                trackStateLiveData.postValue(ViewModelTrackState.TrackTimeRemain(playerInteractor.getTrackTimeRemained()))
            }
        }
    }

    fun startPreparePlayer(url: String?) {
        playerInteractor.preparePlayer(url)
    }

    private fun preparePlayer() {
        playerState = PlayerState.STATE_PREPARED
        playerStateLiveData.postValue(ViewModelPlayerState.Prepare)
    }

    private fun initPlayerState(){
        playerState = PlayerState.STATE_DEFAULT
    }

    private fun pausePlayer() {
        playerState = PlayerState.STATE_PAUSED
        playerStateLiveData.postValue(ViewModelPlayerState.Pause)
    }

}