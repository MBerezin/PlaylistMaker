package com.practicum.playlistmaker.ui.player.model

data class TrackInfo(
    val trackName: String?, // Название композиции
    val artistName: String, // Имя исполнителя
    val artworkUrl500: String, // Ссылка на изображение обложки
    val trackTime: String?, // Продолжительность трека
    val collectionName: String, // Название альбома
    val releaseDate: String,// Год релиза трека
    val primaryGenreName: String,// Жанр трека
    val country: String,// Страна исполнителя
    val previewUrl: String
)
