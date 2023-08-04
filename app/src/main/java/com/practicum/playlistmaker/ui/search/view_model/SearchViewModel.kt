package com.practicum.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.ui.search.model.ViewModelSearchState

class SearchViewModel(
    private val application: Application,
    private val searchInteractor: SearchInteractor,
    ) : AndroidViewModel(application)  {
    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getSearchViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                SearchViewModel(
                    application,
                    Creator.provideSearchInteractor(application),
                )
            }
        }
    }


    private val searchStateLiveData = MutableLiveData<ViewModelSearchState>()
    private var searchText: String = String()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks() }

    fun observeSearchState(): LiveData<ViewModelSearchState> = searchStateLiveData

    fun onTextChanged(searchText:String){
        this.searchText = searchText
    }

    fun searchTracks(){
        if(searchText.isNotEmpty()){
            searchStateLiveData.postValue(ViewModelSearchState.StartSearchState)
            searchInteractor.searchTracks(
                searchText = searchText,
                success = {tracks -> searchStateLiveData.postValue(ViewModelSearchState.SuccessSearchState(tracks))
                },
                error = {
                    searchStateLiveData.postValue(ViewModelSearchState.ErrorSearchState)
                },
                empty = {
                    searchStateLiveData.postValue(ViewModelSearchState.EmptySearchState)
                }
            )
        }
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun openPlayer(track: Track){
        searchInteractor.openPlayer(track)
    }

    fun addHistoryTrack(track: Track) : ArrayList<Track>{
        return searchInteractor.addHistoryTrack(track)
    }

    fun clearHistoryTracks(){
        searchInteractor.clearHistoryTracks()
    }

    fun readHistoryTracks() : ArrayList<Track>{
        return searchInteractor.readHistoryTracks()
    }
}