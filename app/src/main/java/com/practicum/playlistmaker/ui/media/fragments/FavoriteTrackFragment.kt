package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavoriteTrackBinding
import com.practicum.playlistmaker.ui.media.model.ViewModelFavoriteState
import com.practicum.playlistmaker.ui.media.view_model.FavoriteTrackViewModel
import com.practicum.playlistmaker.ui.search.adapters.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteTrackBinding
    private val viewModel: FavoriteTrackViewModel by viewModel()
    private var isClickAllowed = true
    private val adapter = TrackAdapter({ track ->
        if (clickDebounce()) {
            viewModel.openPlayer(track)
        }

    })



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.showFavoriteTracks()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(this.viewLifecycleOwner){favoriteState ->
            when(favoriteState){
                is ViewModelFavoriteState.ListIsEmpty -> {
                    binding.linearLayoutPlaceholder.visibility = View.VISIBLE
                    adapter.setTracks(null)
                    binding.favoriteList.visibility = View.GONE
                }
                is ViewModelFavoriteState.Success -> {
                    adapter.setTracks(tracks = favoriteState.tracks)
                    binding.linearLayoutPlaceholder.visibility = View.GONE
                    binding.favoriteList.visibility = View.VISIBLE
                }
                else -> {}
            }
        }

        binding.favoriteList.layoutManager = LinearLayoutManager(requireContext())
        binding.favoriteList.adapter = adapter

        viewModel.showFavoriteTracks()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {

        fun newInstance() = FavoriteTrackFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}