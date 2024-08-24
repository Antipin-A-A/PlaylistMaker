package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class MusicViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.activity_music_adapter, parent, false)
) {

    private val trackName : TextView = itemView.findViewById(R.id.track_name)
    private val artistTime : TextView = itemView.findViewById(R.id.artist_time)
    private val imageGroup : ImageView = itemView.findViewById(R.id.image_view_music_group)

    fun bind(item : Track) {
        val artistName = if (item.artistName.length > 20) {
            item.artistName.take(17) + "..."
        } else {
            item.artistName
        }
        val time = item.trackTimeMillis
        val convectorTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toLong())

        trackName.text = item.trackName
        artistTime.text = itemView.context.getString(R.string.name_artist_time, artistName, convectorTime)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.image)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageGroup)
    }
}
