package com.practicum.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var toolbar: Toolbar

    private lateinit var sharedPref: SharedPreferences

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
        toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        showTrack()

    }

    private fun showTrack(){
        val json = sharedPref.getString(PLAYER_TRACK, null)
        if (json !== null){
            val trackFromJson = Gson().fromJson(json, Track::class.java)

            textViewTrackName.text = trackFromJson.trackName
            textViewArtistName.text = trackFromJson.artistName
            textViewDurationValue.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackFromJson.trackTimeMillis.toInt())
            textViewAlbumValue.text = trackFromJson.collectionName
            textViewYearValue.text = trackFromJson.getReleaseDate()
            textViewGenreValue.text = trackFromJson.primaryGenreName
            textViewCountryValue.text = trackFromJson.country

            val roundCorner = this.resources.getDimensionPixelSize(R.dimen.roundCornerPlayerArtwork)

            Glide.with(this)
                .load(trackFromJson.getCoverArtwork())
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(roundCorner))
                .into(imageViewArtwork)

        }
    }
}