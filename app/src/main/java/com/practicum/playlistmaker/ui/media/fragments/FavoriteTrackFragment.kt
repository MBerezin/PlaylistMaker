package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentFavoriteTrackBinding
import com.practicum.playlistmaker.ui.media.view_model.FavoriteTrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteTrackBinding
    private val viewModel: FavoriteTrackViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        fun newInstance() = FavoriteTrackFragment()
    }
}