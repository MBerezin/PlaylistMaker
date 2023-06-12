package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
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
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

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
    private var mediaPlayer = MediaPlayer()

    private lateinit var sharedPref: SharedPreferences

    private val handler = Handler(Looper.getMainLooper())
    private val setTimeRemained = Runnable {
        setTimeRemainedText()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val PROGRESS_TIME_REMAINED_DELAY = 300L
    }

    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

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

        sharedPref = getSharedPreferences(SearchActivity.PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        showTrack()

        buttonPlayTrack.setOnClickListener {
            playbackControl()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(setTimeRemained)
        mediaPlayer.release()
    }

    private fun showTrack(){
        val json = sharedPref.getString(SearchActivity.PLAYER_TRACK, null)
        if (json !== null){
            val trackFromJson = Gson().fromJson(json, Track::class.java)

            textViewTrackName.text = trackFromJson.trackName
            textViewArtistName.text = trackFromJson.artistName
            textViewDurationValue.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackFromJson.trackTimeMillis.toInt())
            textViewAlbumValue.text = trackFromJson.collectionName
            textViewYearValue.text = trackFromJson.getReleaseDate()
            textViewGenreValue.text = trackFromJson.primaryGenreName
            textViewCountryValue.text = trackFromJson.country

            preparePlayer(trackFromJson.previewUrl)

            val roundCorner = this.resources.getDimensionPixelSize(R.dimen.roundCornerPlayerArtwork)

            Glide.with(this)
                .load(trackFromJson.getCoverArtwork())
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(roundCorner))
                .into(imageViewArtwork)

        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlayTrack.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            buttonPlayTrack.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            handler.removeCallbacks(setTimeRemained)
            textViewTimeRemained.text = "00:00"
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        setTimeRemainedText()
        buttonPlayTrack.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlayTrack.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        handler.removeCallbacks(setTimeRemained)
    }

    private fun setTimeRemainedText(){
        textViewTimeRemained.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.postDelayed(setTimeRemained, PROGRESS_TIME_REMAINED_DELAY)
    }

}