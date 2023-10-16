package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.ui.media.model.ViewModelPlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<ViewModelPlaylistState>()
    fun observeState(): LiveData<ViewModelPlaylistState> = stateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylists()
                .collect { list ->
                    val playlistsList = mutableListOf<Playlist>()
                    playlistsList.addAll(list)
                    if (playlistsList.isNotEmpty()) {
                        stateLiveData.postValue(ViewModelPlaylistState.Success(playlistsList))
                    } else {
                        stateLiveData.postValue(ViewModelPlaylistState.ListIsEmpty)
                    }
                }
        }
    }
}