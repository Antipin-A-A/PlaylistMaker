package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.activity.MusicFragment
import com.example.playlistmaker.search.domain.api.OnItemClickListener
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.ui.state.TrackListState
import com.example.playlistmaker.search.ui.viewmodel.SearchActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchActivityViewModel>()

    private lateinit var adapter: MusicAdapter
    private lateinit var adapterTrackListHistory: MusicAdapter
    private lateinit var trackListHistory: RecyclerView
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeMediaState().observe(viewLifecycleOwner) {}

        trackListHistory = view.findViewById(R.id.trackList2)

        val onItemClickListener = OnItemClickListener { track ->
            if (clickDebounce()) {
                viewModel.saveHistoryTrack(track)
                viewModel.saveTrack(track)
                findNavController().navigate(
                R.id.action_searchFragment_to_musicFragment
                )
            }
        }

        adapterTrackListHistory = MusicAdapter(onItemClickListener)
        trackListHistory.adapter = adapterTrackListHistory

        adapter = MusicAdapter(onItemClickListener)
        binding.trackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = adapter

        binding.buttonCleanSearch.setOnClickListener {
            binding.editText.setText("")
            val inputMethodManager =
               context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.buttonCleanSearch.windowToken, 0)
            adapter.notifyDataSetChanged()
            binding.buttonUpDate.isVisible = false
            binding.placeholderMessage.isVisible = false
          //  binding.trackList2.isVisible = true
        }

        binding.editText.addTextChangedListener(
            onTextChanged = { p0: CharSequence?, _, _, _ ->
                viewModel.searchDebounce(p0?.toString()?:"")
                if (binding.editText.hasFocus() && binding.editText.text.isEmpty()) {
                    viewModel.getHistoryTrackList()
//                    viewModel.searchDebounce(changedText = p0.toString())
                }
            },
            afterTextChanged = { _: Editable? ->
             //   statusString = binding.editText.text.toString()
                binding.buttonUpDate.isVisible = false
                binding.placeholderMessage.isVisible = false
            }
        )

        binding.buttonUpDate.setOnClickListener {
            viewModel.iTunesServiceSearch(binding.editText.text.toString())
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeHistoryTrackList()
            trackListHistory.isVisible = false
            binding.textLookingForYou.isVisible = false
            binding.buttonClearHistory.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: TrackListState) {
        when (state) {
            is TrackListState.Loading -> showLoading()
            is TrackListState.Content -> showContent(state.track)
            is TrackListState.Empty -> showMessage(
                getString(R.string.error_is_empty),
                "",
                R.drawable.search_error_mode
            )
            is TrackListState.Error -> showMessage(
                getString(R.string.errorWifi),
                "1",
                R.drawable.intent_mode
            )
            is TrackListState.GetHistoryList -> viewGroupTrackList2(state.track)
        }
    }

    private fun showLoading() = with(binding) {
        trackList.isVisible = false
        trackList2.isVisible = false
        placeholderMessage.isVisible = false
        progressBar.isVisible = true
        buttonCleanSearch.isVisible = true
        buttonUpDate.isVisible = false
        textLookingForYou.isVisible = false
        buttonClearHistory.isVisible = false
    }

    private fun showContent(track: List<Track>) = with(binding) {
        trackList.isVisible = true
        trackList2.isVisible = false
        buttonCleanSearch.isVisible = true
        placeholderMessage.isVisible = false
        textLookingForYou.isVisible = false
        buttonClearHistory.isVisible = false
        progressBar.isVisible = false
        buttonUpDate.isVisible = false
        adapter.tracks.clear()
        adapter.tracks.addAll(track)
        adapter.notifyDataSetChanged()
    }

    private fun viewGroupTrackList2(track: List<Track>) = with(binding) {
        adapterTrackListHistory.tracks = track.toMutableList()
        trackList.isVisible = false
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus || binding.editText.text.isEmpty()) {
                if (adapterTrackListHistory.tracks.isEmpty() || binding.editText.text.isNotEmpty()) {
                    trackList2.isVisible = false
                    textLookingForYou.isVisible = false
                    buttonClearHistory.isVisible = false
                } else {
                    trackList2.isVisible = true
                    textLookingForYou.isVisible = true
                    buttonClearHistory.isVisible = true
                    adapterTrackListHistory.notifyDataSetChanged()
                }
            }
        }
        }

    private fun showMessage(text: String, additionalMessage: String, drawable: Int) =
        with(binding) {
            progressBar.isVisible = false
            trackList.isVisible = false
            trackList2.isVisible = false
            placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)

            if (text.isNotEmpty()) {
                placeholderMessage.isVisible = true
                adapter.tracks.clear()
                adapter.notifyDataSetChanged()
                placeholderMessage.text = text
                buttonUpDate.isVisible = false
                if (additionalMessage.isNotEmpty()) {
                    buttonUpDate.isVisible = true
                }
            } else {
                placeholderMessage.isVisible = false
                buttonUpDate.isVisible = false
            }
        }

    companion object {
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
