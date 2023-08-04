package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.domain.player.model.Track

data class TracksResponse(val resultCount: Int,
                        val results: List<Track>)