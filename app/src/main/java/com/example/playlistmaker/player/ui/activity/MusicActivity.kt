package com.example.playlistmaker.player.ui.activity


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator.Creator.setContext
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMusicBinding
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.player.ui.viewmodel.MusicActivityViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale


class MusicActivity : AppCompatActivity() {

    private lateinit var viewModel: MusicActivityViewModel

    private lateinit var binding: ActivityMusicBinding

    private var mainThreadHandler: Handler? = null
    private var isRunTime = false
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContext(applicationContext)
        applicationContext

        Log.i("Log1", "MusicActivity")

        mainThreadHandler = Handler(Looper.getMainLooper())


        viewModel = ViewModelProvider(
            this,
            MusicActivityViewModel.mediaViewModelFactory()
        )[MusicActivityViewModel::class.java]

        init()

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is TrackScreenState.Content -> {
                    content(screenState)
                }

                is TrackScreenState.Loading -> {

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
//                viewModel.playAudio(url)
                snake(it, "Плейлист «BeSt SoNg EvEr!» создан")

            }

            buttonPlay.setOnClickListener {
                     playerStart(url)
            }

        }
    }

    private fun content(track: TrackScreenState.Content) = with(binding) {
        artistName.text = track.trackModel.artistName
        collectionNameFirst.text = track.trackModel.trackName
        collectionName.text = track.trackModel.collectionName
        trackTime.text = track.trackModel.trackTimeMillis
        releaseDate.text = track.trackModel.releaseDate
        primaryGenreName.text = track.trackModel.primaryGenreName
        country.text = track.trackModel.country

        val url = track.trackModel.previewUrl.toString()
        Log.i("Log4", "$url")

        Glide.with(this@MusicActivity)
            .load(track.trackModel.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
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

    private fun playerStart(url:String) = with(binding) {
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

        viewModel.setOnComplited {
            binding.buttonPlay.setImageResource(R.drawable.play_icon)
            binding.currentTrackTime.text = getString(R.string.current_track_time)
            isRunTime = false
        }
    }
    companion object {
        private const val DELAY = 1000L
    }

}