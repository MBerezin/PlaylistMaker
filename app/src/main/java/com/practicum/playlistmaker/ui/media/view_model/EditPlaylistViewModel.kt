package com.practicum.playlistmaker.ui.media.view_model

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.ui.media.model.ViewModelNewPlaylistState
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): NewPlaylistViewModel(
    playlistInteractor
) {
    fun savePlaylist(playlistId: Int, titleInputText: String, descriptionInputText: String, imagePrivateStorageUri: String){
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(
                Playlist(
                    id = playlistId,
                    desc = descriptionInputText,
                    name = titleInputText,
                    coverUri = imagePrivateStorageUri.toUri(),
                    size = 0,
                    tracksList = ""
                )
            )
            stateLiveData.postValue(ViewModelNewPlaylistState.SaveSuccess)
        }
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylist(id)
                .collect{ playlist ->
                    stateLiveData.postValue(ViewModelNewPlaylistState.PlaylistSuccessRead(playlist = playlist))
                }
        }
    }
}