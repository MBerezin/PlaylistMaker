package com.practicum.playlistmaker.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.model.TrackInfo
import com.practicum.playlistmaker.presentation.presenter.player.PlayerPresenter
import com.practicum.playlistmaker.presentation.presenter.player.PlayerView
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var presenter: PlayerPresenter

    private lateinit var imageViewArtwork: ImageView
    private lateinit var textViewTrackName: TextView
    private lateinit var textViewArtistName: TextView
    private lateinit var textViewDurationValue: TextView
    private lateinit var textViewAlbumValue: TextView
    private lateinit var textViewYearValue: TextView
    private lateinit var textViewGenreValue: TextView
    private lateinit var textViewCountryValue: TextView
    private lateinit var textViewTimeRemained: TextView
    private lateinit var buttonPlayTrack: ImageButton
    private lateinit var toolbar: Toolbar

    private val handler = Handler(Looper.getMainLooper())

    companion object {

        private const val PROGRESS_TIME_REMAINED_DELAY = 300L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        presenter = PlayerPresenter(this, this)
        presenter.loadTrack()

        imageViewArtwork = findViewById<ImageView>(R.id.imageViewArtwork)
        textViewTrackName = findViewById<TextView>(R.id.textViewTrackName)
        textViewArtistName = findViewById<TextView>(R.id.textViewArtistName)
        textViewDurationValue = findViewById<TextView>(R.id.textViewDurationValue)
        textViewAlbumValue = findViewById<TextView>(R.id.textViewAlbumValue)
        textViewYearValue = findViewById<TextView>(R.id.textViewYearValue)
        textViewGenreValue = findViewById<TextView>(R.id.textViewGenreValue)
        textViewCountryValue = findViewById<TextView>(R.id.textViewCountryValue)
        textViewTimeRemained = findViewById<TextView>(R.id.textViewTimeRemained)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        buttonPlayTrack = findViewById<ImageButton>(R.id.buttonPlayTrack)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        buttonPlayTrack.setOnClickListener {
            presenter.playbackControl()
        }

        presenter.startPreparePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun showTrack(trackInfo: TrackInfo){
        val roundCorner = this.resources.getDimensionPixelSize(R.dimen.roundCornerPlayerArtwork)

        textViewTrackName.text = trackInfo.trackName
        textViewArtistName.text = trackInfo.artistName
        textViewDurationValue.text = trackInfo.trackTime
        textViewAlbumValue.text = trackInfo.collectionName
        textViewYearValue.text = trackInfo.releaseDate
        textViewGenreValue.text = trackInfo.primaryGenreName
        textViewCountryValue.text = trackInfo.country

        Glide.with(this)
            .load(trackInfo.artworkUrl500)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundCorner))
            .into(imageViewArtwork)
    }

    override fun preparePlayer() {
        buttonPlayTrack.isEnabled = true
        buttonPlayTrack.setImageResource(R.drawable.play)
        textViewTimeRemained.text = "00:00"
    }

    override fun setTrackCompleted(){

    }

    override fun startPlayer() {
        buttonPlayTrack.setImageResource(R.drawable.pause)
        handler.post(setTrackTimeRemained())
    }

    override fun pausePlayer() {
        buttonPlayTrack.setImageResource(R.drawable.play)
    }

    private fun setTrackTimeRemained(): Runnable{
        return object : Runnable{
            override fun run() {
                textViewTimeRemained.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(presenter.getTrackTimeRemained())
                handler.postDelayed(this, PROGRESS_TIME_REMAINED_DELAY)
            }
        }
    }
}