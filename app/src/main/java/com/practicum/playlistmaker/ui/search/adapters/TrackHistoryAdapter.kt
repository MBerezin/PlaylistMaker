package com.practicum.playlistmaker.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.player.model.Track

class TrackHistoryAdapter(
    private val itemClickListener: ((Track) -> Unit)
) : RecyclerView.Adapter<TrackViewHolder>() {

    private val tracks: ArrayList<Track> = arrayListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener() {
            itemClickListener.invoke(tracks[position])
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