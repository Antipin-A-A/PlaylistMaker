package com.example.playlistmaker.ui.music

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.OnItemClickListener
import com.example.playlistmaker.domain.modeles.Track

class MusicAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MusicViewHolder>() {

    var tracks = mutableListOf<Track>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_music_adapter, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onItemClickListener.onItemClick(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}