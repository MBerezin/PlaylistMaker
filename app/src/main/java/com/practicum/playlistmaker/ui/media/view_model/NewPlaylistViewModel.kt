package com.practicum.playlistmaker.ui.media.view_model

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.ui.media.model.ViewModelNewPlaylistState
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<ViewModelNewPlaylistState>()
    fun observeState(): LiveData<ViewModelNewPlaylistState> = stateLiveData

    fun savePlaylist(titleInputText: String, descriptionInputText: String, imagePrivateStorageUri: String) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                Playlist(
                    id = null,
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
}