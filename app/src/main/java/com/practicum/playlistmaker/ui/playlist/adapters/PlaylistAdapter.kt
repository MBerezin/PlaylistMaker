package com.practicum.playlistmaker.ui.playlist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.player.model.Track

class PlaylistAdapter(
    private val itemClickListener: (Track) -> Unit,
    private val itemLongClickListener: (Track) -> Unit
): RecyclerView.Adapter<PlaylistViewHolder>() {

    private val tracks: ArrayList<Track> = arrayListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.invoke(tracks[position])
            return@setOnLongClickListener true
        }
    }

    fun setTracks(tracks: List<Track>?) {
        this.tracks.clear()
        if (!tracks.isNullOrEmpty()) {
            this.tracks.addAll(tracks)
        }
        notifyDataSetChanged()
    }

    fun clearTracks(){
        tracks.clear()
        notifyDataSetChanged()
    }

}