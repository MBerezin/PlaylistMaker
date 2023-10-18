package com.practicum.playlistmaker.ui.player.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.media.model.Playlist

class PlaylistAdapter(
    private val itemClickListener: ((Playlist, List<Int>) -> Unit),
    val context: Context
): RecyclerView.Adapter<PlaylistViewHolder>() {

    private val gson = Gson()

    var playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val typeTokenArrayList = object : TypeToken<ArrayList<Int>>() {}.type
        val playlistTrackIds = gson.fromJson<ArrayList<Int>>(playlists[position].tracksList, typeTokenArrayList) ?: arrayListOf()

        holder.bind(playlists[position])
        holder.itemView.setOnClickListener() {
            itemClickListener.invoke(playlists[position], playlistTrackIds)
        }
    }
}