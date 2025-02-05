package com.example.playlistmaker.player.ui.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMusicBinding
import com.example.playlistmaker.player.ui.state.TrackScreenState
import com.example.playlistmaker.player.ui.viewmodel.MusicFragmentViewModel
import com.example.playlistmaker.player.ui.viewmodel.PlayListStateForMusic
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.playlist.ui.viewmodel.PlayListState
import com.example.playlistmaker.search.domain.api.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MusicPlayListAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    private var isRunTime = false
    private val viewModel by viewModel<MusicFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()


        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                snake(message)
            }
        }

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

            val overlay = binding.overlay

            bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet2)

            buttonAdd.setOnClickListener {
                viewModel.fillData()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            buttonPlay.setOnClickListener {
                playerStart()
            }

            buttonLike.setOnClickListener {
                viewModel.onFavoriteClicked()
            }

            buttonNewPlayList.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                findNavController().navigate(
                    R.id.action_musicFragment_to_fragmentNewPlayList
                )
            }

            bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            overlay.visibility = View.GONE
                        }

                        else -> {
                            overlay.visibility = View.VISIBLE
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

            val onItemClickListener = OnItemClickListener<PlayList> { playlist ->
                if (clickDebounce()) {
                    viewModel.saveFillData(playlist.id)
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            adapter = MusicPlayListAdapter(onItemClickListener)
            binding.playList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.playList.adapter = adapter
        }
    }

    private fun content(track: TrackScreenState.Content) = with(binding) {
        artistName.text = track.trackModel?.artistName ?: getString(R.string.artist_name)
        collectionNameFirst.text =
            track.trackModel?.trackName ?: getString(R.string.collection_name)
        collectionName.text = track.trackModel?.collectionName ?: getString(R.string.albom)
        trackTime.text = track.trackModel?.trackTimeMillis ?: getString(R.string.duration)
        releaseDate.text = track.trackModel?.releaseDate ?: getString(R.string.god)
        primaryGenreName.text =
            track.trackModel?.primaryGenreName ?: getString(R.string.primary_genre_name)
        country.text = track.trackModel?.country ?: getString(R.string.country)

        Glide.with(this@MusicFragment)
            .load(track.trackModel?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.empty_image_group)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageView)
    }

    override fun onResume() {
        super.onResume()
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onPause() {
        super.onPause()
        if (isRunTime) {
            viewModel.pause()
        }
        isRunTime = false
    }

    private fun snake(string: String) {
        val snack: Snackbar = Snackbar.make(requireView(), string, Snackbar.LENGTH_LONG)
        snack.setTextColor(resources.getColor(R.color.background))
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

    private fun render(state: PlayListStateForMusic) {
        when (state) {
            is PlayListStateForMusic.Content -> showContent(state.playlist)
            is PlayListStateForMusic.Loading -> showLoading()
        }
    }

    private fun showLoading() = with(binding) {
        playList.isVisible = false
    }

    private fun showContent(playList: List<PlayList>) = with(binding) {
        binding.playList.visibility = View.VISIBLE
        adapter.playLists.clear()
        adapter.playLists.addAll(playList)
        adapter.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}