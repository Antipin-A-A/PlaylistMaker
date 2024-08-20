package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(private val onItemClickListener : OnItemClickListener) :
    RecyclerView.Adapter<MusicViewHolder>() {

    var tracks = mutableListOf<Track>()


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MusicViewHolder {
        return MusicViewHolder(parent)
    }

    override fun onBindViewHolder(holder : MusicViewHolder, position : Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(tracks[holder.adapterPosition]) }
    }

    override fun getItemCount() : Int {
        return tracks.size
    }
}