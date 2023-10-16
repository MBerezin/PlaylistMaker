package com.practicum.playlistmaker.util

object GetTracksQuantity {
    fun getInWord(quantity: Int): String {
        quantity % 100
        if (quantity in 5..20) return "треков"
        quantity % 10
        when (quantity) {
            1 -> return "трек"

            in 2..4 -> return "трека"

            else -> return "треков"
        }
    }
}