package com.practicum.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.ui.player.model.ViewModelFavoriteState
import com.practicum.playlistmaker.ui.player.model.ViewModelPlayerState
import com.practicum.playlistmaker.ui.player.model.ViewModelTrackState
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModelPlayer: PlayerViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelPlayer.loadTrack()

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.buttonPlayTrack.setOnClickListener {
            viewModelPlayer.playbackControl()
        }

        binding.buttonLikeTrack.setOnClickListener {
            viewModelPlayer.onFavoriteClicked()
        }

        viewModelPlayer.observeTrackState().observe(this){trackState ->
            when(trackState){
                is ViewModelTrackState.ShowError -> {
                    Toast.makeText(this, trackState.message, Toast.LENGTH_SHORT).show()
                }
                is ViewModelTrackState.ShowTrack -> {

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

                    viewModelPlayer.startPreparePlayer(trackState.trackInfo.previewUrl)

                }
                is ViewModelTrackState.TrackTimeRemain -> {
                    binding.textViewTimeRemained.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackState.time)
                }
            }
        }

        viewModelPlayer.observePlayerState().observe(this) { playerState ->
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

        viewModelPlayer.observeFavoriteState().observe(this){favoriteState ->
            when(favoriteState){
                ViewModelFavoriteState.FavoriteTrack -> {
                    binding.buttonLikeTrack.setImageResource(R.drawable.like)
                }
                ViewModelFavoriteState.NotFavoriteTrack -> {
                    binding.buttonLikeTrack.setImageResource(R.drawable.notlike)
                }
            }
        }

    }
}