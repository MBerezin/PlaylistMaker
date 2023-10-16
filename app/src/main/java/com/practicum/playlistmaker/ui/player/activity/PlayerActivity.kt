package com.practicum.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.ui.media.fragments.NewPlaylistFragment
import com.practicum.playlistmaker.ui.player.adapters.PlaylistAdapter
import com.practicum.playlistmaker.ui.player.model.ViewModelFavoriteState
import com.practicum.playlistmaker.ui.player.model.ViewModelPlayerState
import com.practicum.playlistmaker.ui.player.model.ViewModelPlaylistState
import com.practicum.playlistmaker.ui.player.model.ViewModelTrackState
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    private val playlistsAdapter = PlaylistAdapter({ playlist, playlistTrackIds ->
        if (clickDebounce()) {
            viewModel.addTrackToPlaylist(playlist, playlistTrackIds, currentTrackId)
        }

    })

    private var isClickAllowed = true
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var currentTrackId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadTrack()

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.buttonLikeTrack.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        viewModel.observeTrackState().observe(this){ trackState ->
            when(trackState){
                is ViewModelTrackState.ShowError -> {
                    Toast.makeText(this, trackState.message, Toast.LENGTH_SHORT).show()
                }
                is ViewModelTrackState.ShowTrack -> {

                    currentTrackId =  trackState.trackInfo.trackId

                    val roundCorner = this.resources.getDimensionPixelSize(R.dimen.roundCornerPlayerArtwork)

                    binding.textViewTrackName.text = trackState.trackInfo.trackName
                    binding.textViewArtistName.text = trackState.trackInfo.artistName
                    binding.textViewDurationValue.text = trackState.trackInfo.trackTime
                    binding.textViewAlbumValue.text = trackState.trackInfo.collectionName
                    binding.textViewYearValue.text = trackState.trackInfo.releaseDate
                    binding.textViewGenreValue.text = trackState.trackInfo.primaryGenreName
                    binding.textViewCountryValue.text = trackState.trackInfo.country

                    Glide.with(this)
                        .load(trackState.trackInfo.artworkUrl500)
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .transform(RoundedCorners(roundCorner))
                        .into(binding.imageViewArtwork)

                    viewModel.startPreparePlayer(trackState.trackInfo.previewUrl)

                }
                is ViewModelTrackState.TrackTimeRemain -> {
                    binding.textViewTimeRemained.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackState.time)
                }
            }
        }

        viewModel.observePlayerState().observe(this) { playerState ->
            when(playerState){
                ViewModelPlayerState.Pause -> {
                    binding.buttonPlayTrack.setImageResource(R.drawable.play)
                }
                ViewModelPlayerState.Play -> {
                    binding.buttonPlayTrack.setImageResource(R.drawable.pause)
                }
                ViewModelPlayerState.Prepare, ViewModelPlayerState.Stop -> {
                    binding.buttonPlayTrack.isEnabled = true
                    binding.buttonPlayTrack.setImageResource(R.drawable.play)
                    binding.textViewTimeRemained.text = "00:00"
                }
            }
        }

        viewModel.observeFavoriteState().observe(this){ favoriteState ->
            when(favoriteState){
                ViewModelFavoriteState.FavoriteTrack -> {
                    binding.buttonLikeTrack.setImageResource(R.drawable.like)
                }
                ViewModelFavoriteState.NotFavoriteTrack -> {
                    binding.buttonLikeTrack.setImageResource(R.drawable.notlike)
                }
            }
        }

        viewModel.observePlaylistState().observe(this){ playlistState ->
            when(playlistState){
                ViewModelPlaylistState.ListIsEmpty -> {

                }
                is ViewModelPlaylistState.Success -> {
                    playlistsAdapter.playlists.clear()
                    playlistsAdapter.playlists.addAll(playlistState.playlist)
                    playlistsAdapter.notifyDataSetChanged()
                }
                is ViewModelPlaylistState.TrackAdded -> {
                    Toast.makeText(this, String.format(getString(R.string.track_add), playlistState.playlist.name), Toast.LENGTH_SHORT).show()

                }
                is ViewModelPlaylistState.TrackExistsByPlaylist -> {
                    Toast.makeText(this, String.format(getString(R.string.track_exists_by_playlist), playlistState.playlist.name), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.playlists.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.playlists.adapter = playlistsAdapter

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })

        binding.buttonAddTrack.setOnClickListener {
            playlistsAdapter.playlists.clear()
            viewModel.getPlaylists()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlaylist.setOnClickListener {
            val newPlaylistFragment = NewPlaylistFragment()
            val trackId = Bundle()
            trackId.putString(TRACK_ID, currentTrackId.toString())
            newPlaylistFragment.arguments = trackId

            binding.overlay.isVisible = false
            binding.playlistsBottomSheet.isVisible = false
            binding.scrollView.isVisible = false

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newPlaylistFragment)
                .commit()
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
        const val TRACK_ID = "TRACK_ID"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}