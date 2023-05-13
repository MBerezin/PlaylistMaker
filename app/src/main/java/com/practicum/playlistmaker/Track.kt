package com.practicum.playlistmaker

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackTimeMillis: String // Продолжительность трека
)
