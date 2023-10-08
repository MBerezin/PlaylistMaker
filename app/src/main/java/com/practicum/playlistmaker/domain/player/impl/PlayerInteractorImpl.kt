package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.api.PlayerRepository
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.api.PlayerStateListener
import com.practicum.playlistmaker.domain.player.consumer.Consumer
import com.practicum.playlistmaker.domain.player.model.Track

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {

    override fun getTrackDetails(
        consumer: Consumer<Track>
    ): Track? {
        return playerRepository.getTrackDetails(consumer)
    }

    override fun playTrack() {
        playerRepository.playTrack()
    }

    override fun pauseTrack() {
        playerRepository.pauseTrack()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun getTrackTimeRemained(): Int {
        return playerRepository.getTrackTimeRemained()
    }

    override fun setListener(listener: PlayerStateListener) {
        playerRepository.setListener(listener)
    }

    override fun clearListener() {
        playerRepository.clearListener()
    }

    override fun preparePlayer(url: String?) {
        playerRepository.preparePlayer(url)
    }
}