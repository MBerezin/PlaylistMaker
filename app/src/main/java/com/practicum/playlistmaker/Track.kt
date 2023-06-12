package com.practicum.playlistmaker

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackTimeMillis: String, // Продолжительность трека
    val trackId: Int, // уникальный идентификатор трека,
    val collectionName: String, // Название альбома
    @get:JvmName("track_releaseDate")
    val releaseDate: String,// Год релиза трека
    val primaryGenreName: String,// Жанр трека
    val country: String,// Страна исполнителя
    val previewUrl: String)//ссылку на отрывок
{
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")


    fun getReleaseDate(): String{
        return if(releaseDate.isNotEmpty()){
            releaseDate.substring(0,4)
        } else {
            ""
        }
    }

}
