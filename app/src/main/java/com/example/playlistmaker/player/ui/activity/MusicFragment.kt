package com.example.playlistmaker.player.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMusicBinding
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.player.ui.viewmodel.MusicActivityViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    private var isRunTime = false
    private val viewModel by viewModel<MusicActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is TrackScreenState.Loading -> {
                }

                is TrackScreenState.Content -> {
                    content(screenState)
                }

                is TrackScreenState.TimeTrack -> {
                    binding.currentTrackTime.text = screenState.time
                }

                is TrackScreenState.Complete -> {
                    complete()
                }
            }
        }
        viewModel.isFavorite.observe(viewLifecycleOwner) {
            if (it) {
                binding.buttonLike.setImageResource(R.drawable.haed_red)
            } else {
                binding.buttonLike.setImageResource(R.drawable.head_with)
            }
        }
    }


    private fun init() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            buttonAdd.setOnClickListener {
                snake(it, "Плейлист «BeSt SoNg EvEr!» создан")

            }

            buttonPlay.setOnClickListener {
                playerStart()
            }

            buttonLike.setOnClickListener {
                viewModel.onFavoriteClicked()
            }
        }
    }

    private fun content(track: TrackScreenState.Content) = with(binding) {
        artistName.text = track.trackModel?.artistName ?: getString(R.string.artist_name)
        collectionNameFirst.text =
            track.trackModel?.trackName ?: getString(R.string.collection_name)
        collectionName.text = track.trackModel?.collectionName ?: getString(R.string.albom)
        trackTime.text = track.trackModel?.trackTimeMillis ?: getString(R.string.track_time)
        releaseDate.text = track.trackModel?.releaseDate ?: getString(R.string.god)
        primaryGenreName.text =
            track.trackModel?.primaryGenreName ?: getString(R.string.primary_genre_name)
        country.text = track.trackModel?.country ?: getString(R.string.country)

        Glide.with(this@MusicFragment)
            .load(track.trackModel?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageView)
    }

    override fun onPause() {
        super.onPause()
        if (isRunTime) {
            viewModel.pause()
        }
        isRunTime = false
    }

    private fun snake(view: View, string: String) {
        val snack: Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG)
        snack.setTextColor(getResources().getColor(R.color.background))
        snack.show()
    }

    private fun playerStart() = with(binding) {
        if (isRunTime) {
            viewModel.pause()
            isRunTime = false
            buttonPlay.setImageResource(R.drawable.play_icon)
        } else {
            viewModel.start()
            isRunTime = true
            buttonPlay.setImageResource(R.drawable.pause_icon)
        }

        viewModel.setOnCompleted()
    }

    private fun complete() {
        isRunTime = false
        binding.buttonPlay.setImageResource(R.drawable.play_icon)
        binding.currentTrackTime.text = getString(R.string.current_track_time)

    }

}