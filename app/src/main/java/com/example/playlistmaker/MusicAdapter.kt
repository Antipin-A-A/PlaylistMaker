package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(
    private val data : List<Track>,
) : RecyclerView.Adapter<MusicViewHolder>() {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MusicViewHolder {
        return MusicViewHolder(parent)
    }

    override fun onBindViewHolder(holder : MusicViewHolder, position : Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() : Int {
        return data.size
    }
}