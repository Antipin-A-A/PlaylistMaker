package com.example.playlistmaker.ui.music_search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.OnItemClickListener
import com.example.playlistmaker.domain.api.interactor.TrackIteractor
import com.example.playlistmaker.domain.modeles.Track
import com.example.playlistmaker.ui.media_player.MusicActivity
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.Creator.setContext
import com.google.gson.Gson

const val NEW_FACT_KEY = "NEW_FACT_KEY"

class SearchActivity : AppCompatActivity() {

    private val trackIteractor by lazy { Creator.provideTrackInteractor() }

    lateinit var binding: ActivitySearchBinding
    private var statusString: String = INPUT_TEXT
    private val results = mutableListOf<Track>()
    lateinit var adapter: MusicAdapter
    private lateinit var adapter2: MusicAdapter
    private lateinit var buttonUpdate: Button
    private lateinit var trackList2: RecyclerView
    private lateinit var textFind: TextView
    private lateinit var clearButtonHistory: Button
    private lateinit var placeholderMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContext(applicationContext)
        buttonUpdate = findViewById(R.id.buttonUpDate)
        trackList2 = findViewById(R.id.trackList2)
        textFind = findViewById(R.id.textLookingForYou)
        clearButtonHistory = findViewById(R.id.buttonClearHistory)
        placeholderMessage = findViewById(R.id.placeholderMessage)

        val onItemClickListener = OnItemClickListener { track ->
            if (clickDebounce()) {
                trackIteractor.saveTrack(track)
                val intent = Intent(this, MusicActivity::class.java)
                intent.putExtra(NEW_FACT_KEY, createJsonFromFact(track))
                startActivity(intent)
            }
        }

        adapter2 = MusicAdapter(onItemClickListener)
        adapter2.tracks = trackIteractor.getSavedTracks().toMutableList()
        trackList2.adapter = adapter2

        adapter = MusicAdapter(onItemClickListener)
        adapter.tracks = results
        binding.trackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.trackList.adapter = adapter

        binding.buttonCleanSearch.setOnClickListener {
            binding.editText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.buttonCleanSearch.windowToken, 0)
            results.clear()
            adapter.notifyDataSetChanged()
            buttonUpdate.isVisible = false
            placeholderMessage.isVisible = false
            viewGroupTrackList2()
        }

        binding.toolbar.setNavigationOnClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editText.text.isEmpty()) {
                viewGroupTrackList2()
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()

            }

            override fun afterTextChanged(p0: Editable?) {
                statusString = binding.editText.text.toString()
            }

        }
        binding.editText.addTextChangedListener(simpleTextWatcher)

        buttonUpdate.setOnClickListener {
            iTunesServiceSearch()
            buttonUpdate.isVisible = false
        }

        clearButtonHistory.setOnClickListener {
            trackIteractor.removeTrackList()
            viewGroupTrackList2()
        }
    }

    private fun iTunesServiceSearch() = with(binding) {

        trackList.isVisible = false

        if (editText.hasFocus() && editText.text.isNotEmpty()) {
            viewGroupTrackList2()
            placeholderMessage.isVisible = false
            progressBar.isVisible = true
            buttonCleanSearch.isVisible = true
            buttonUpdate.isVisible = false

            trackIteractor.searchTrack(editText.text.toString(),
                object : TrackIteractor.TrackConsumer {
                    override fun consume(foundTreks: List<Track>?, errorMessage: String?) {
                        handler.post {
                            progressBar.isVisible = false
                            if (foundTreks != null) {
                                results.clear()
                                results.addAll(foundTreks)
                                trackList.isVisible = true
                                adapter.notifyDataSetChanged()
                            }
                            if (errorMessage != null) {
                                showMessage(
                                    getString(R.string.errorWifi),
                                    "1",
                                    R.drawable.intent_mode
                                )
                            } else if (results.isEmpty() && binding.editText.text.isNotEmpty()) {
                                showMessage(
                                    getString(R.string.error_is_empty),
                                    "",
                                    R.drawable.search_error_mode
                                )
                            } else {
                                placeholderMessage.isVisible = false
                            }
                        }

                    }
                })
        } else {
            viewGroupTrackList2()
            placeholderMessage.isVisible = false
            buttonUpdate.isVisible = false
        }
    }

    private fun showMessage(text: String, additionalMessage: String, drawable: Int) {

        placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)

        if (text.isNotEmpty()) {
            placeholderMessage.isVisible = true
            results.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            buttonUpdate.isVisible = false
            if (additionalMessage.isNotEmpty()) {
                buttonUpdate.isVisible = true
            }
        } else {
            placeholderMessage.isVisible = false
            buttonUpdate.isVisible = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STRING, statusString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        statusString = savedInstanceState.getString(KEY_STRING) ?: ""
        findViewById<EditText>(R.id.editText).setText(statusString)
    }

    private fun createJsonFromFact(fact: Track): String {
        return Gson().toJson(fact)
    }

    private fun viewGroupTrackList2() {
        adapter2.tracks = trackIteractor.getSavedTracks().toMutableList()
        if (adapter2.tracks.isEmpty() || binding.editText.text.isNotEmpty()) {
            trackList2.isVisible = false
            textFind.isVisible = false
            clearButtonHistory.isVisible = false
        } else {
            trackList2.isVisible = true
            textFind.isVisible = true
            clearButtonHistory.isVisible = true
            adapter2.notifyDataSetChanged()
        }

    }

    companion object {
        const val KEY_STRING = "KEY_STRING"
        const val INPUT_TEXT = ""
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private val searchRunnable = Runnable { iTunesServiceSearch() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}
