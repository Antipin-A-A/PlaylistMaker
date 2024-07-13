package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MusicViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.activity_music_adapter, parent, false)) {

    private val trackName : TextView = itemView.findViewById(R.id.track_name)
    private val artistTime : TextView = itemView.findViewById(R.id.artist_time)
    private val imageGroup : ImageView = itemView.findViewById(R.id.image_view_music_group)

    fun bind(item : Track) {
        val track = item.trackName
        val artistName = if (item.artistName.length >= 20) {
            item.artistName.take(17) + "..."
        } else {
            item.artistName
        }
        val time = item.trackTime
        val cloudsIcon = item.artworkUrl100

        trackName.text = track
        artistTime.text = itemView.context.getString(R.string.name_artist_time, artistName, time)
        Glide.with(itemView)
            .load(cloudsIcon)
            .placeholder(R.drawable.image)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageGroup)
    }
}