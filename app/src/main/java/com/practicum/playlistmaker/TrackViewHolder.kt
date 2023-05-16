package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView = itemView.findViewById(R.id.textViewTrackName)
    private val artistNameView: TextView = itemView.findViewById(R.id.textViewArtistName)
    private val trackTimeView: TextView = itemView.findViewById(R.id.textViewTrackTime)
    private val artworkView: ImageView = itemView.findViewById<ImageView>(R.id.imageViewArtwork)

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        if (model.trackTimeMillis === null){
            trackTimeView.text = "00:00"
        } else {
            trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())
        }

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(10))
            .into(artworkView)
    }
}