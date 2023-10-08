package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.ui.media.model.ViewModelFavoriteState
import kotlinx.coroutines.launch

class FavoriteTrackViewModel(
    private val searchInteractor: SearchInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
): ViewModel() {

    private val stateLiveData = MutableLiveData<ViewModelFavoriteState>()
    fun observeState(): LiveData<ViewModelFavoriteState> = stateLiveData

    fun openPlayer(track: Track){
        searchInteractor.openPlayer(track)
    }

    fun showFavoriteTracks(){
        viewModelScope.launch {
            favoriteTracksInteractor.getTracks().collect(){
                favoriteTracks ->
                if(favoriteTracks.isEmpty()){
                    stateLiveData.postValue(ViewModelFavoriteState.ListIsEmpty)
                } else {
                    stateLiveData.postValue(ViewModelFavoriteState.Success(tracks = favoriteTracks.reversed()))
                }
            }
        }
    }
}