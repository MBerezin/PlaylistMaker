package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.domain.player.model.Track

class PlaylistTrackDbConvertor {
    fun map(track: PlaylistTrackEntity): Track {
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

    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
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