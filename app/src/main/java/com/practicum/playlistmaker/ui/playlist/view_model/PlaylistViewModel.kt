package com.practicum.playlistmaker.ui.playlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.api.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.sharing.api.SharingInteractor
import com.practicum.playlistmaker.ui.media.model.ViewModelPlaylistState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val searchInteractor: SearchInteractor,
    private val sharingInteractor: SharingInteractor,
): ViewModel() {

    private lateinit var actualPlaylist: Playlist
    private lateinit var actualTracks: List<Track>
    private val stateLiveData = MutableLiveData<ViewModelPlaylistState>()
    fun observeState(): LiveData<ViewModelPlaylistState> = stateLiveData

    fun getTracksByPlaylistId(id: Int) {
        viewModelScope.launch {
            playlistInteractor
                .getTracksByPlaylistId(id)
                .collect { tracks ->
                    val tracksList = mutableListOf<Track>()
                    tracksList.addAll(tracks)
                    stateLiveData.postValue(ViewModelPlaylistState.TracksSuccessRead(tracks = tracks.sortedByDescending { track -> track.addedAt }, trackCount = tracks.size, tracksPlaytime = playlistInteractor.getPlaytimeFromTracks(tracks)))
                    actualTracks = tracks
                }
        }
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylist(id)
                .collect{ playlist ->
                    stateLiveData.postValue(ViewModelPlaylistState.PlaylistSuccessRead(playlist = playlist))
                    actualPlaylist = playlist
                }
        }
    }

    fun openPlayer(track: Track) {
        searchInteractor.openPlayer(track)
    }

    fun deleteTrackFromPlaylist(trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(actualPlaylist, trackId)
            stateLiveData.postValue(ViewModelPlaylistState.PlaylistSuccessTrackDelete)
        }

    }

    fun sharePlaylist(){
        if (actualPlaylist.size > 0){
            sharingInteractor.sharePlaylist(actualPlaylist, actualTracks)
        } else {
            stateLiveData.postValue(ViewModelPlaylistState.PlaylistNoTracks)
        }
    }

    fun deletePlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(id)
            stateLiveData.postValue(ViewModelPlaylistState.PlaylistDeleted)
        }
    }
}