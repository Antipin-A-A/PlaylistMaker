package com.example.playlistmaker.search.ui.activity


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMusicAdapterBinding
import com.example.playlistmaker.search.domain.modeles.Track

class MusicViewHolder(item: View) : RecyclerView.ViewHolder(item){

    private val binding = ActivityMusicAdapterBinding.bind(item)

    fun bind(item: Track) = with(binding) {
        val artistName = if (item.artistName?.length!! > 20) {
            item.artistName.take(17) + "..."
        } else {
            item.artistName
        }

        trackName.text = item.trackName

        artistNameAndTime.text = itemView.context.getString(R.string.name_artist_time, artistName, item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.image)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageMusicGroup)
    }
}
