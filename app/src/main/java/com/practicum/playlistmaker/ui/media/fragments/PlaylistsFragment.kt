package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.ui.media.adapters.PlaylistAdapter
import com.practicum.playlistmaker.ui.media.model.ViewModelPlaylistsState
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.ui.playlist.fragments.PlaylistFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: PlaylistsViewModel by viewModel()
    private lateinit var playlistsAdapter: PlaylistAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        playlistsAdapter.playlists.clear()
        viewModel.getPlaylists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsAdapter = PlaylistAdapter({ playlist ->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_mediaFragment_to_playlistFragment,
                    PlaylistFragment.createArgs(playlist.id!!)
                )
            }
        })

        viewModel.observeState().observe(this.viewLifecycleOwner){playlistState ->
            when(playlistState){
                is ViewModelPlaylistsState.ListIsEmpty -> {
                    binding.noresultsPlaceholder.visibility = View.VISIBLE
                    playlistsAdapter.playlists.clear()
                    binding.playlists.visibility = View.GONE
                }
                is ViewModelPlaylistsState.Success -> {
                    playlistsAdapter.playlists.clear()
                    playlistsAdapter.playlists.addAll(playlistState.playlists)
                    playlistsAdapter.notifyDataSetChanged()

                    binding.noresultsPlaceholder.visibility = View.GONE
                    binding.playlists.visibility = View.VISIBLE
                }
                else -> {}
            }
        }

        binding.playlists.layoutManager = GridLayoutManager(requireContext(),2)
        binding.playlists.adapter = playlistsAdapter

        binding.buttonPlaylistNew.setOnClickListener() {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}