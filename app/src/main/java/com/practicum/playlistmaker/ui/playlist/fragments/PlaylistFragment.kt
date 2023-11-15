package com.practicum.playlistmaker.ui.playlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.media.fragments.EditPlaylistFragment
import com.practicum.playlistmaker.ui.media.model.ViewModelPlaylistState
import com.practicum.playlistmaker.ui.playlist.adapters.PlaylistAdapter
import com.practicum.playlistmaker.ui.playlist.view_model.PlaylistViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel: PlaylistViewModel by viewModel()
    private val adapter = PlaylistAdapter({ track ->
        if (clickDebounce()) {
            viewModel.openPlayer(track)
        }

    }, { track ->
        var dialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.delete_track)
            .setMessage(R.string.question_delete_track)
            .setNegativeButton(R.string.cancel) { dialog, which ->
                binding.overlayPlaylist.visibility = View.GONE
            }
            .setPositiveButton(R.string.delete) { dialog, which ->
                viewModel.deleteTrackFromPlaylist(track.trackId)
            }
        dialog.show()
        binding.overlayPlaylist.visibility = View.VISIBLE
    })

    private var isClickAllowed = true
    private lateinit var bottomSheetBehaviorPlaylist: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorTracks: BottomSheetBehavior<LinearLayout>

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylist(requireArguments().getInt(ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(this.viewLifecycleOwner) { playlistState ->
            when (playlistState) {
                is ViewModelPlaylistState.PlaylistSuccessRead -> {
                    binding.textViewTitle.text = playlistState.playlist.name
                    binding.textViewDescription.text = playlistState.playlist.desc

                    Glide.with(this)
                        .load(playlistState.playlist.coverUri.toString())
                        .placeholder(R.drawable.placeholder_small)
                        .transform(CenterCrop())
                        .into(binding.imageViewCover)

                    binding.item.playlistName.text = playlistState.playlist.name
                    binding.item.playlistSize.text = resources.getQuantityString(
                        R.plurals.track_count,
                        playlistState.playlist.size,
                        playlistState.playlist.size
                    )

                    Glide.with(this)
                        .load(playlistState.playlist.coverUri.toString())
                        .placeholder(R.drawable.placeholder)
                        .transform(CenterCrop())
                        .into(binding.item.cover)

                    viewModel.getTracksByPlaylistId(requireArguments().getInt(ID))
                }
                is ViewModelPlaylistState.TracksSuccessRead -> {
                    val playTime = SimpleDateFormat(
                        "mm",
                        Locale.getDefault()
                    ).format(playlistState.tracksPlaytime)
                    binding.textViewTrackCount.text = resources.getQuantityString(
                        R.plurals.track_count,
                        playlistState.trackCount,
                        playlistState.trackCount
                    )
                    binding.textViewTracksTime.text = resources.getQuantityString(
                        R.plurals.minutes,
                        playTime.toInt(),
                        playTime.toInt()
                    )
                    if (playlistState.trackCount > 0) {
                        binding.tracksBottomSheet.visibility = View.VISIBLE
                    } else {
                        binding.tracksBottomSheet.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.playlist_tracks_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    adapter.clearTracks()
                    adapter.setTracks(playlistState.tracks)
                }
                ViewModelPlaylistState.PlaylistSuccessTrackDelete -> {
                    viewModel.getPlaylist(requireArguments().getInt(ID))
                    binding.overlayPlaylist.visibility = View.GONE
                }
                ViewModelPlaylistState.PlaylistNoTracks -> {
                    var dialog = MaterialAlertDialogBuilder(requireActivity())
                        .setMessage(getString(R.string.no_tracks_by_playlist))
                        .setNeutralButton(getString(R.string.close)) { dialog, which ->

                        }
                    dialog.show()
                }
                ViewModelPlaylistState.PlaylistDeleted -> {
                    binding.tracksBottomSheet.visibility = View.GONE
                    binding.overlayPlaylist.visibility = View.GONE
                    bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_HIDDEN
                    findNavController().navigateUp()
                }
            }

        }

        binding.tracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracks.adapter = adapter

        binding.imageViewBackNav.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageViewShare.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.imageViewMenu.setOnClickListener {
            bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.textViewShare.setOnClickListener {
            bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.sharePlaylist()
        }
        binding.textViewUpdate.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(requireArguments().getInt(ID))
            )
        }
        binding.textViewDelete.setOnClickListener {
            var dialog = MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.delete_playlist)
                .setMessage(R.string.question_delete_playlist)
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    binding.overlayPlaylist.visibility = View.GONE
                }
                .setPositiveButton(R.string.delete) { dialog, which ->
                    viewModel.deletePlaylist(requireArguments().getInt(ID))
                }
            dialog.show()
            binding.overlayPlaylist.visibility = View.VISIBLE
        }

        bottomSheetBehaviorPlaylist = BottomSheetBehavior.from(binding.playlistBottomSheet)
        bottomSheetBehaviorPlaylist.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehaviorPlaylist.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlayPlaylist.visibility = View.GONE
                    }
                    else -> {
                        binding.overlayPlaylist.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //binding.overlayPlaylist.alpha = slideOffset
            }
        })

        bottomSheetBehaviorTracks = BottomSheetBehavior.from(binding.tracksBottomSheet)
        binding.tracksBottomSheet.visibility = View.GONE

        binding.tracksBottomSheet.doOnNextLayout {
            bottomSheetBehaviorTracks.peekHeight =
                binding.root.bottom.toInt() - binding.imageViewMenu.bottom.toInt() - resources.getDimensionPixelSize(
                    R.dimen.paddingHorizontal
                )
        }

        bottomSheetBehaviorTracks.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
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
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val ID = "id"

        fun createArgs(id: Int): Bundle {
            return bundleOf(ID to id)
        }
    }
}