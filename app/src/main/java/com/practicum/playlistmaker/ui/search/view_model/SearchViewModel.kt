package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.*
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.models.SearchStates
import com.practicum.playlistmaker.ui.search.model.ViewModelSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    ) : ViewModel()  {
    companion object {

        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 2000L

    }


    private val searchStateLiveData = MutableLiveData<ViewModelSearchState>()
    private var searchText: String = String()

    private var searchJob: Job? = null

    fun observeSearchState(): LiveData<ViewModelSearchState> = searchStateLiveData

    fun onTextChanged(searchText:String){
        this.searchText = searchText
    }

    fun searchTracks(){
        if(searchText.isNotEmpty()){
            searchStateLiveData.postValue(ViewModelSearchState.StartSearchState)
            viewModelScope.launch {
                searchInteractor.searchTracks(
                    searchText = searchText
                ).collect {
                    result: SearchStates ->
                    when (result){
                        is SearchStates.Success -> {searchStateLiveData.postValue(ViewModelSearchState.SuccessSearchState(result.data))}
                        is SearchStates.EmptyError -> {searchStateLiveData.postValue(ViewModelSearchState.EmptySearchState)}
                        is SearchStates.ServerError -> {searchStateLiveData.postValue(ViewModelSearchState.ErrorSearchState)}
                    }
                }
            }
        }
    }

    fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_IN_MILLIS)
            searchTracks()
        }
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