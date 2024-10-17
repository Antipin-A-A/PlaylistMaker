package com.example.playlistmaker.ui.media_player


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMusicBinding
import com.example.playlistmaker.domain.modeles.Track
import com.example.playlistmaker.ui.music_search.NEW_FACT_KEY
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class MusicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicBinding

    private var mediaPlayer: MediaPlayer? = null
    private var mainThreadHandler: Handler? = null
    private var isRunTime = false

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
                trackTime.text = track.trackTimeMillis
                releaseDate.text = track.releaseDate
                primaryGenreName.text = track.primaryGenreName
                textViewCountry.text = track.country

                val url = track.previewUrl

                Glide.with(this@MusicActivity)
                    .load(track.getCoverArtwork())
                    .placeholder(R.drawable.placeholder)
                    .fitCenter()
                    .transform(RoundedCorners(10))
                    .into(imageView)

                buttonAdd.setOnClickListener {
                    snake(it, "Плейлист «BeSt SoNg EvEr!» создан")

                }

                mediaPlayer = MediaPlayer(url)
                mediaPlayer?.preparePlayer()

                buttonPlay.setOnClickListener {
                    playerStart()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pausePlayer()
        isRunTime = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.mediaPlayer?.release()
    }

    private fun snake(view: View, string: String) {
        val snack: Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG)
        snack.setTextColor(getResources().getColor(R.color.white_Black))
        snack.show()
    }

    private fun createFactFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (isRunTime ) {
                    val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.mediaPlayer?.currentPosition?.toLong())
                    binding.currentTrackTime.text = trackTime
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }

    private fun playerStart() = with(binding) {
        mediaPlayer?.playbackControl()

        if (isRunTime) {
            mediaPlayer?.pausePlayer()
            isRunTime = false
            buttonPlay.setImageResource(R.drawable.play_icon)
            mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
        } else {
            mediaPlayer?.startPlayer()
            isRunTime = true
            buttonPlay.setImageResource(R.drawable.pause_icon)
//            createUpdateTimerTask(currentTrackTime, mainThreadHandler)
            mainThreadHandler?.post(createUpdateTimerTask())
        }

        mediaPlayer?.mediaPlayer?.setOnCompletionListener {
            buttonPlay.setImageResource(R.drawable.play_icon)
            binding.currentTrackTime.text = getString(R.string.current_track_time)
            isRunTime = false
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}