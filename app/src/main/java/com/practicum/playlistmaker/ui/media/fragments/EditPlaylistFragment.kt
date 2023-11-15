package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.media.model.ViewModelNewPlaylistState
import com.practicum.playlistmaker.ui.media.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: NewPlaylistFragment() {
    override val viewModel: EditPlaylistViewModel by viewModel()

    override fun setOnViewCreated(){
        viewModel.observeState().observe(this.viewLifecycleOwner) { newPlaylistState ->
            when(newPlaylistState){
                ViewModelNewPlaylistState.SaveError -> {
                    Toast.makeText(requireContext(), R.string.retry, Toast.LENGTH_LONG).show()
                }
                ViewModelNewPlaylistState.SaveSuccess -> {
                    Toast.makeText(requireContext(), getString(R.string.playlist_updated_success, titleInputText), Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                is ViewModelNewPlaylistState.ImageSaved -> {
                    imagePrivateStorageUri = newPlaylistState.uri
                    viewModel.savePlaylist(requireArguments().getInt(ID), titleInputText, descriptionInputText, imagePrivateStorageUri)
                }
                is ViewModelNewPlaylistState.PlaylistSuccessRead -> {
                    binding.name.setText(newPlaylistState.playlist.name)
                    binding.description.setText(newPlaylistState.playlist.desc)
                    imagePrivateStorageUri = newPlaylistState.playlist.coverUri.toString()

                    Glide.with(this)
                        .load(newPlaylistState.playlist.coverUri.toString())
                        .placeholder(R.drawable.placeholder_small)
                        .transform(CenterCrop())
                        .into(binding.imageViewCover)
                }
            }
        }

        setCoverClickListener()
        setDescriptionTextWatcher()
        setTitleTextWatcher()
        setBackNavigation()
        setViewTexts()
        setSaveBtn()

        viewModel.getPlaylist(requireArguments().getInt(ID))
    }

    private fun setSaveBtn() {
        binding.btnCreatePlaylist.setOnClickListener {
            if(titleInputText.trim().isNotEmpty()){
                if(coverUri != null){
                    viewModel.saveImageToPrivateStorage(coverUri!!, getString(R.string.playlists_folder_name), getString(R.string.playlist_cover_filename_part))
                } else {
                    viewModel.savePlaylist(requireArguments().getInt(ID), titleInputText, descriptionInputText, imagePrivateStorageUri)
                }
            }
        }
    }

    override fun setBackNavigation(){
        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun setViewTexts(){
        binding.btnCreatePlaylist.setText(R.string.save_playlist)
        binding.toolbar.setTitle(R.string.edit_playlist)
    }

    companion object {
        private const val ID = "id"

        fun createArgs(id: Int): Bundle {
            return bundleOf(ID to id)
        }

    }
}