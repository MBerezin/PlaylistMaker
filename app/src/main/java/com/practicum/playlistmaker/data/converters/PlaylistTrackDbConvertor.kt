package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.domain.player.model.Track

class PlaylistTrackDbConvertor {
    fun map(track: PlaylistTrackEntity): Track {
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
            track.previewUrl,
            false,
            track.addedAt
        )
    }

    fun map(track: Track, playlistId: Int): PlaylistTrackEntity {
        val collectionName = track.collectionName ?: ""
        return PlaylistTrackEntity(
            track.trackId,
            playlistId,
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