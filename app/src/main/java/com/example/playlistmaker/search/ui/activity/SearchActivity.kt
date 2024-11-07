package com.example.playlistmaker.search.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator.Creator.setContext
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.MusicActivity
import com.example.playlistmaker.search.domain.api.OnItemClickListener
import com.example.playlistmaker.search.domain.modeles.Track
import com.example.playlistmaker.search.ui.state.TrackListState
import com.example.playlistmaker.search.ui.viewmodel.SearchActivityViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchActivityViewModel

    lateinit var binding: ActivitySearchBinding
    private var statusString: String = INPUT_TEXT
    lateinit var adapter: MusicAdapter
    private lateinit var adapter2: MusicAdapter
    private lateinit var trackList2: RecyclerView
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContext(applicationContext)
        applicationContext

        viewModel = ViewModelProvider(
            this,
            SearchActivityViewModel.searchViewModelFactory()
        )[SearchActivityViewModel::class.java]

        viewModel.observeMediaState().observe(this) {

        }


        trackList2 = findViewById(R.id.trackList2)

        val onItemClickListener = OnItemClickListener { track ->
            if (clickDebounce()) {
                viewModel.saveHistoryTrack(track)
              //  val intent = Intent(this, MusicActivity::class.java)
                viewModel.saveTrack(track)
              //  intent.putExtra(NEW_FACT_KEY, createJsonFromFact(track))
                startActivity(Intent(this, MusicActivity::class.java))
            }
        }

        adapter2 = MusicAdapter(onItemClickListener)
        trackList2.adapter = adapter2

        adapter = MusicAdapter(onItemClickListener)
        binding.trackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = adapter

        binding.buttonCleanSearch.setOnClickListener {
            binding.editText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.buttonCleanSearch.windowToken, 0)
            adapter.notifyDataSetChanged()
            binding.buttonUpDate.isVisible = false
            binding.placeholderMessage.isVisible = false
        }

        binding.toolbar.setNavigationOnClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.state.observe(this) {
                    render(it)
                }
            }
        }

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.searchDebounce(p0.toString())
                if (binding.editText.hasFocus() && binding.editText.text.isNotEmpty()) {
                    viewModel.getHistoryTrackList()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                statusString = binding.editText.text.toString()
                binding.buttonUpDate.isVisible = false
                binding.placeholderMessage.isVisible = false
            }

        })

        binding.buttonUpDate.setOnClickListener {
            showLoading()

        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.removeHistoryTrackList()
            trackList2.isVisible = false
            binding.textLookingForYou.isVisible = false
            binding.buttonClearHistory.isVisible = false
        }
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

            is TrackListState.getHistoryList -> viewGroupTrackList2(state.track)
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
        adapter2.tracks = track.toMutableList()
        trackList.isVisible = false
        if (adapter2.tracks.isEmpty() || binding.editText.text.isNotEmpty()) {
            trackList2.isVisible = false
            textLookingForYou.isVisible = false
            buttonClearHistory.isVisible = false
        } else {
            trackList2.isVisible = true
            textLookingForYou.isVisible = true
            buttonClearHistory.isVisible = true
            adapter2.notifyDataSetChanged()
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

//    private fun createJsonFromFact(fact: Track): String {
//        return Gson().toJson(fact)
//    }


    companion object {
        const val INPUT_TEXT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}