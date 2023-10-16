package com.practicum.playlistmaker.data.converters

import androidx.core.net.toUri
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.domain.media.model.Playlist

class PlaylistDbConvertor {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.desc,
            playlist.coverUri.toUri(),
            playlist.tracksList,
            playlist.size
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.desc,
            playlist.coverUri.toString(),
            playlist.tracksList,
            playlist.size
        )
    }
}