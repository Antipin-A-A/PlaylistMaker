package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityMusicBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MusicActivity : AppCompatActivity() {
    lateinit var binding: ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            val factTrack = intent.getStringExtra(NEW_FACT_KEY)
            if (factTrack != null) {
                val track = createFactFromJson(factTrack)
                artistName.text = track.artistName
                collectionNameFirst.text = track.trackName
                collectionName.text = track.collectionName

                val data = track.releaseDate
                val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val localDateTime = LocalDateTime.parse(data, pattern)

                trackTime.text = track.convectorTime()
                releaseDate.text = localDateTime.year.toString()
                primaryGenreName.text = track.primaryGenreName
                textViewCountry.text = track.country

                Glide.with(this@MusicActivity)
                    .load(track.getCoverArtwork())
                    .placeholder(R.drawable.image)
                    .fitCenter()
                    .transform(RoundedCorners(10))
                    .into(imageView)

                buttonAdd.setOnClickListener {
                    snake(it, "Плейлист «BeSt SoNg EvEr!» создан")

                }
            }
        }
    }

    private fun snake(view: View, string: String) {
        val snack: Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG)
        snack.setTextColor(getResources().getColor(R.color.white_Black))
        snack.show()
    }

    private fun createFactFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}