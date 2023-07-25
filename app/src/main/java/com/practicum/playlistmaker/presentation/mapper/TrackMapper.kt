package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.model.TrackInfo
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