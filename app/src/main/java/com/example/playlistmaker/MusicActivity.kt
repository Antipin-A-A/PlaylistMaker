package com.example.playlistmaker


import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private lateinit var binding: ActivityMusicBinding

    private var player: Player? = null
    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

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

                val url = track.previewUrl

                Glide.with(this@MusicActivity)
                    .load(track.getCoverArtwork())
                    .placeholder(R.drawable.image)
                    .fitCenter()
                    .transform(RoundedCorners(10))
                    .into(imageView)

                buttonAdd.setOnClickListener {
                    snake(it, "Плейлист «BeSt SoNg EvEr!» создан")

                }

                player = Player(url, buttonPlay, currentTrackTime, mainThreadHandler)
                player?.preparePlayer()
                buttonPlay.setOnClickListener {
                    player?.playbackControl()
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        player?.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.mediaPlayer?.release()
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