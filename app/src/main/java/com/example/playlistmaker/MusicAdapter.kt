package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter() : RecyclerView.Adapter<MusicViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MusicViewHolder {
        return MusicViewHolder(parent)
    }

    override fun onBindViewHolder(holder : MusicViewHolder, position : Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() : Int {
        return tracks.size
    }
}