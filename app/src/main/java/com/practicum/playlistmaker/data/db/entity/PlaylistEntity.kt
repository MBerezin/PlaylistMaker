package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val desc: String,
    val coverUri: String,
    val tracksList: String? = null,
    val size: Int = 0
)
