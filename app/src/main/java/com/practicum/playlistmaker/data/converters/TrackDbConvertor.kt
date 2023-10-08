package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.player.model.Track

class TrackDbConvertor {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.artworkUrl100,
            track.trackTimeMillis,
            track.id,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.artworkUrl100,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.previewUrl
        )
    }
}