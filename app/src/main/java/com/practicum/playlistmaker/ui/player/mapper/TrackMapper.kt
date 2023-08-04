package com.practicum.playlistmaker.ui.player.mapper

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.player.model.TrackInfo
import java.text.SimpleDateFormat
import java.util.*

object TrackMapper {

    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackName = track.trackName,
            artistName = track.artistName,
            artworkUrl500 = track.getCoverArtwork(),
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt()),
            collectionName = track.collectionName,
            releaseDate = track.getReleaseDate(),
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }


}