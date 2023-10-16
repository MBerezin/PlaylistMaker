package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_table")
data class PlaylistTrackEntity(
    @PrimaryKey
    val id: Int,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: String,
    val previewUrl: String?
)
