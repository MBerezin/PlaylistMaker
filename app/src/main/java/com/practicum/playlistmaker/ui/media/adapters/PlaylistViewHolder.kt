package com.practicum.playlistmaker.ui.media.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.util.GetTracksQuantity

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val cover: ImageView = itemView.findViewById(R.id.playlistCover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistTitle)
    private val playlistSize: TextView = itemView.findViewById(R.id.tracksQuantity)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.name
        playlistSize.text = playlist.size.toString() + " " + GetTracksQuantity.getInWord(playlist.size)

        Glide.with(itemView)
            .load(playlist.coverUri.toString())
            .centerCrop()
            .transform(RoundedCorners(28))
            .placeholder(R.drawable.placeholder)
            .into(cover)
    }
}