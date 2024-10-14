package com.example.playlistmaker.ui.media_player


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMusicBinding
import com.example.playlistmaker.domain.modeles.Track
import com.example.playlistmaker.ui.music.NEW_FACT_KEY
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


class MusicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicBinding

    private var mediaPlayer: MediaPlayer? = null
    private var mainThreadHandler: Handler? = null
    private var isRunTime = false
    private var secondsCount = 31L

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

    private fun createUpdateTimerTask(trackTimeView: TextView, handler: Handler?) {
        val startTime = System.currentTimeMillis()
        var seconds = 0L
        handler?.post(
            object : Runnable {
                override fun run() {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val remainingTime = (secondsCount * DELAY) - elapsedTime
                    if (isRunTime && remainingTime > 0) {
                        seconds = remainingTime / DELAY
                        trackTimeView.text =
                            String.format("%d:%02d", seconds / 60, seconds % 60)
                        handler.postDelayed(this, DELAY)
                    } else {
                        secondsCount = seconds
                    }
                }
            })
    }

    private fun playerStart() = with(binding) {
        mediaPlayer?.playbackControl()

        if (isRunTime) {
            mediaPlayer?.pausePlayer()
            isRunTime = false
            buttonPlay.setImageResource(R.drawable.play_icon)
        } else {
            mediaPlayer?.startPlayer()
            isRunTime = true
            buttonPlay.setImageResource(R.drawable.pause_icon)
            createUpdateTimerTask(currentTrackTime, mainThreadHandler)
        }

        mediaPlayer?.mediaPlayer?.setOnCompletionListener {
            buttonPlay.setImageResource(R.drawable.play_icon)
            secondsCount = 31L
            isRunTime = false
        }
    }

    companion object {
        private const val DELAY = 1000L
    }

}