package com.practicum.playlistmaker.ui.player.adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.media.model.Playlist

class PlaylistViewHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView){
    private val cover: ImageView = itemView.findViewById(R.id.cover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val playlistSize: TextView = itemView.findViewById(R.id.playlist_size)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.name
        playlistSize.text = context.resources.getQuantityString(R.plurals.track_count, playlist.size, playlist.size)

        Glide.with(itemView)
            .load(playlist.coverUri.toString())
            .transform(CenterCrop(), RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.roundCornerPlayerArtwork)))
            .placeholder(R.drawable.placeholder)
            .into(cover)
    }
}