package com.example.playlistmaker.player.ui.activity


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMusicBinding
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.player.ui.viewmodel.MusicActivityViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class MusicActivity : AppCompatActivity() {

    private val viewModel by viewModel<MusicActivityViewModel>()

    private lateinit var binding: ActivityMusicBinding

    private var mainThreadHandler: Handler? = null
    private var isRunTime = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

        init()

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Content -> {
                    content(screenState)
                }

                is TrackScreenState.Loading -> {

                }

                TrackScreenState.Complete -> {
                    complete()
                }
            }
        }
    }


    private fun init() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            buttonAdd.setOnClickListener {
                snake(it, "Плейлист «BeSt SoNg EvEr!» создан")

            }

            buttonPlay.setOnClickListener {
                playerStart()
            }

        }
    }

    private fun content(track: TrackScreenState.Content) = with(binding) {
        artistName.text = track.trackModel?.artistName ?: getString(R.string.artist_name)
        collectionNameFirst.text = track.trackModel?.trackName ?: getString(R.string.collection_name)
        collectionName.text = track.trackModel?.collectionName ?: getString(R.string.albom)
        trackTime.text = track.trackModel?.trackTimeMillis ?: getString(R.string.track_time)
        releaseDate.text = track.trackModel?.releaseDate ?: getString(R.string.god)
        primaryGenreName.text = track.trackModel?.primaryGenreName ?: getString(R.string.primary_genre_name)
        country.text = track.trackModel?.country ?: getString(R.string.country)

        Glide.with(this@MusicActivity)
            .load(track.trackModel?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageView)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        isRunTime = false
    }

    private fun snake(view: View, string: String) {
        val snack: Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG)
        snack.setTextColor(getResources().getColor(R.color.white_Black, theme))
        snack.show()
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (isRunTime) {
                    val trackTime = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(viewModel.position())
                    binding.currentTrackTime.text = trackTime.toString()
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
            }
        }
    }

    private fun playerStart() = with(binding) {
        if (isRunTime) {
            viewModel.pause()
            isRunTime = false
            buttonPlay.setImageResource(R.drawable.play_icon)
            mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
        } else {
            viewModel.start()
            isRunTime = true
            buttonPlay.setImageResource(R.drawable.pause_icon)
            mainThreadHandler?.post(createUpdateTimerTask())
        }

        viewModel.setOnCompleted()
    }

    private fun complete() {
        binding.buttonPlay.setImageResource(R.drawable.play_icon)
        binding.currentTrackTime.text = getString(R.string.current_track_time)
        isRunTime = false
    }

    companion object {
        private const val DELAY = 1000L
    }

}