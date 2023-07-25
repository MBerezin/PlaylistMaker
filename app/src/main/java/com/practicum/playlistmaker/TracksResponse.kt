package com.practicum.playlistmaker

import com.practicum.playlistmaker.domain.model.Track

data class TracksResponse(val resultCount: Int,
                        val results: List<Track>)