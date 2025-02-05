package com.example.playlistmaker.screenplaylist.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentScreenPlaylistBinding
import com.example.playlistmaker.playlist.domain.model.PlayList
import com.example.playlistmaker.screenplaylist.ui.viewmodel.PlayListScreenState
import com.example.playlistmaker.screenplaylist.ui.viewmodel.ScreenPlaylistViewModel
import com.example.playlistmaker.screenplaylist.ui.viewmodel.TrackListStateScreen
import com.example.playlistmaker.search.domain.api.OnItemClickListener
import com.example.playlistmaker.search.domain.api.OnItemLongClickListener
import com.example.playlistmaker.search.domain.modeles.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScreenPlaylistFragment : Fragment() {

    private var _binding: FragmentScreenPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehaviorPlayList: BottomSheetBehavior<View>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<View>

    private lateinit var adapter: ScreenPlayListAdapter
    private var isClickAllowed = true
    private val viewModel by viewModel<ScreenPlaylistViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScreenPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehaviorPlayList = BottomSheetBehavior.from(binding.bottomSheetBehaviorPlayList)
        bottomSheetBehaviorPlayList.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.bottomSheetBehaviorMenu)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.getPlaylist()

        val onItemClickListener = OnItemClickListener<Track> { track ->
            if (clickDebounce()) {
                viewModel.saveTrack(track)
                findNavController().navigate(
                    R.id.action_screenPlaylistFragment_to_musicFragment
                )
            }
        }
        val onItemLongClickListener = OnItemLongClickListener { track ->
            if (clickDebounce()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Хотите удалить трек?")
                    .setMessage("")
                    .setNegativeButton("Нет") { dialog, which ->
                    }
                    .setPositiveButton("Да") { dialog, which ->
                        viewModel.deleteTrack(track.trackId.toString())
                        viewModel.getPlaylist()
                        adapter.notifyDataSetChanged()
                    }
                    .show()
            }
        }

        adapter = ScreenPlayListAdapter(onItemClickListener, onItemLongClickListener)
        binding.trackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = adapter

        viewModel.observeTrackState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observePlayListState().observe(viewLifecycleOwner) {
            renderPlayList(it)
        }


        // sheetBehavior(binding.standardBottomSheet2)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonShare.setOnClickListener {
            if (binding.trackList.isVisible) {
                viewModel.sharePlayList()
            }
        }
        binding.buttonDots.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
         //   binding.bottomSheetBehaviorMenu.isVisible = true
            sheetBehavior()
            actionBehaviorButton()
        }

    }

    private fun render(state: TrackListStateScreen) {
        when (state) {
            is TrackListStateScreen.Loading -> showLoading()
            is TrackListStateScreen.Content -> showContent(state.track)
            is TrackListStateScreen.Empty -> empty()
        }
    }

    private fun showLoading() = with(binding) {
        trackList.isVisible = false
    }

    private fun showContent(track: List<Track>) = with(binding) {
        trackList.isVisible = true
        adapter.tracks.clear()
        adapter.tracks.addAll(track)
        adapter.notifyDataSetChanged()
    }

    private fun empty() = with(binding) {
        trackList.isVisible = false
    }

    private fun renderPlayList(state: PlayListScreenState) {
        when (state) {
            is PlayListScreenState.Loading -> showLoading()
            is PlayListScreenState.Content -> content(state.playlist)
        }
    }

    private fun content(playList: PlayList) = with(binding) {
        namePlayList.text = playList.listName ?: getString(R.string.artist_name)
        namePlaylistBehavior.text = playList.listName ?: getString(R.string.artist_name)
        description.text = playList.description ?: ""
        val countTrack = playList.listTracksId?.filterNotNull()?.size.toString()
        viewModel.observeString().observe(viewLifecycleOwner) {
            timeAndQuantity.text = getString(R.string.count_track_time, countTrack, it)
        }
        countTracksBehavior.text = playList.listTracksId?.filterNotNull()?.size.toString()

        IMAGE = playList.urlImage.toString()
        PLAYLISTID = playList.id.toString()

        Glide.with(this@ScreenPlaylistFragment)
            .load(playList.urlImage)
            .placeholder(R.drawable.empty_image_group)
            .into(binding.imageView)


        Glide.with(this@ScreenPlaylistFragment)
            .load(playList.urlImage)
            .placeholder(R.drawable.empty_image_group)
            .into(binding.imageBehavior)
    }

    private fun actionBehaviorButton() = with(binding) {
        textShareBehavior.setOnClickListener {
            binding.bottomSheetBehaviorMenu.isVisible = false
            viewModel.sharePlayList()
        }

        textInformationBehavior.setOnClickListener {
            binding.bottomSheetBehaviorMenu.isVisible = false
            val bundle = Bundle().apply {
                putString("source", "screenPlaylist")
                putString("listName", "${namePlayList.text}")
                putString("description", "${description.text}")
                putString("Image", IMAGE)
                putString("playlistId", PLAYLISTID)
                Log.i("IMAGE", "IMAGE = $IMAGE")
            }
            findNavController().navigate(
                R.id.action_screenPlaylistFragment_to_fragmentNewPlayList, bundle

            )

        }

        textDeleteBehavior.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Хотите удалить плейлист?")
                .setMessage("")
                .setNegativeButton("Нет") { dialog, which ->
                }
                .setPositiveButton("Да") { dialog, which ->
                    viewModel.deleteList()
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    private fun sheetBehavior() {
        val overlay = binding.overlay
        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                        //  adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var IMAGE = "image"
        var PLAYLISTID = "playlistId"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
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
}