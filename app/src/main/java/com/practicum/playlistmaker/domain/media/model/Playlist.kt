package com.practicum.playlistmaker.domain.media.model

import android.net.Uri

data class Playlist(
    val id: Int? = null,
    val name: String,
    val desc: String,
    val coverUri: Uri,
    var tracksList: String? = null,
    var size: Int = 0,
)
