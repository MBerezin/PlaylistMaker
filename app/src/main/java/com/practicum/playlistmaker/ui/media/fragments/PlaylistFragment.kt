package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.media.adapters.PlaylistAdapter
import com.practicum.playlistmaker.ui.media.model.ViewModelPlaylistState
import com.practicum.playlistmaker.ui.media.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var playlistsAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        playlistsAdapter.playlists.clear()
        viewModel.getPlaylists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsAdapter = PlaylistAdapter(requireContext())

        viewModel.observeState().observe(this.viewLifecycleOwner){playlistState ->
            when(playlistState){
                is ViewModelPlaylistState.ListIsEmpty -> {
                    binding.noresultsPlaceholder.visibility = View.VISIBLE
                    playlistsAdapter.playlists.clear()
                    binding.playlists.visibility = View.GONE
                }
                is ViewModelPlaylistState.Success -> {
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

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}