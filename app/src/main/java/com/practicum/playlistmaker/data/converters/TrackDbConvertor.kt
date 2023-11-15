package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.player.model.Track

class TrackDbConvertor {
    fun map(track: TrackEntity): Track {
        val collectionName = track.collectionName ?: ""
        return Track(
            track.trackName,
            track.artistName,
            track.artworkUrl100,
            track.artworkUrl60,
            track.trackTimeMillis,
            track.id,
            collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: Track): TrackEntity {
        val collectionName = track.collectionName ?: ""
        return TrackEntity(
            track.trackId,
            track.artworkUrl100,
            track.artworkUrl60,
            track.trackName,
            track.artistName,
            collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.previewUrl
        )
    }
}