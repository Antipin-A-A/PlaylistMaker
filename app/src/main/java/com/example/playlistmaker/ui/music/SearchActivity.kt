package com.example.playlistmaker.ui.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.NEW_FACT_KEY
import com.example.playlistmaker.data.dto.SharedPrefTrack
import com.example.playlistmaker.data.network.TrackStorageRepositoryImpl
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.OnItemClickListener
import com.example.playlistmaker.domain.api.TrackIteractor
import com.example.playlistmaker.domain.impl.ItemsTrack
import com.example.playlistmaker.presentation.SaveTrackInteractor
import com.example.playlistmaker.domain.modeles.Track
import com.example.playlistmaker.presentation.DeleteTrackUseCase
import com.example.playlistmaker.presentation.GetTrackUseCase
import com.example.playlistmaker.ui.player.MusicActivity
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private val trackInterator = Creator.provideTrackInteractor()

    private val trackStorageRepositoryImpl by lazy {
        TrackStorageRepositoryImpl(
            SharedPrefTrack(applicationContext)
        )
    }
    private val saveTrackInteractor by lazy { SaveTrackInteractor(trackStorageRepositoryImpl) }
    private val getTrackUseCAse by lazy { GetTrackUseCase(trackStorageRepositoryImpl) }
    private val deleteTrack by lazy { DeleteTrackUseCase(trackStorageRepositoryImpl) }

    lateinit var binding: ActivitySearchBinding
    private var statusString: String = INPUT_TEXT
    private var savedTrackList = mutableListOf<Track>()
    private val results = mutableListOf<Track>()
    lateinit var adapter: MusicAdapter
    lateinit var adapter2: MusicAdapter
    private lateinit var buttonUpdate: Button
    private lateinit var trackList2: RecyclerView
    private lateinit var textFind: TextView
    private lateinit var clearButtonHistory: Button
    private lateinit var placeholderMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferencesManager = SharedPrefTrack(this)
//        val trackStorageRepository = TrackStorageRepositoryImpl()

        buttonUpdate = findViewById(R.id.buttonUpDate)
        trackList2 = findViewById(R.id.trackList2)
        textFind = findViewById(R.id.textLookingForYou)
        clearButtonHistory = findViewById(R.id.buttonClearHistory)
        placeholderMessage = findViewById(R.id.placeholderMessage)


         savedTrackList =
             getTrackUseCAse.execute().toMutableList()

        val onItemClickListener = OnItemClickListener { track ->
            if (clickDebounce()) {
                val itemsTrack = ItemsTrack()
                itemsTrack.itemsListTrack(savedTrackList, track)
                saveTrackInteractor.execute(savedTrackList)
                val intent = Intent(this, MusicActivity::class.java)
                intent.putExtra(NEW_FACT_KEY, createJsonFromFact(track))
                startActivity(intent)
            }
        }

        adapter2 = MusicAdapter(onItemClickListener)
        adapter2.tracks = savedTrackList
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
            adapter2.notifyDataSetChanged()
            buttonUpdate.visibility = GONE
            placeholderMessage.visibility = GONE
            if (savedTrackList.isNotEmpty()) viewGroupTrackList2(VISIBLE)
        }

        binding.toolbar.setNavigationOnClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editText.text.isEmpty() && savedTrackList.isNotEmpty()) {
                viewGroupTrackList2(VISIBLE)
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
            buttonUpdate.visibility = GONE
        }

        clearButtonHistory.setOnClickListener {
           preferencesManager.removeAllTrackList()
            savedTrackList.clear()
            adapter2.notifyDataSetChanged()
            viewGroupTrackList2(GONE)
        }
    }

    private fun iTunesServiceSearch() = with(binding) {

        adapter2.notifyDataSetChanged()
        trackList.visibility = GONE

        if (editText.hasFocus() && editText.text.isNotEmpty()) {
            viewGroupTrackList2(GONE)
            placeholderMessage.visibility = GONE
            progressBar.visibility = VISIBLE
            buttonCleanSearch.visibility = VISIBLE


            trackInterator.searchTrack(editText.text.toString(),
                object : TrackIteractor.TrackConsumer {
                    override fun consume(foundTreks: List<Track>) {
                        handler.post {
                            progressBar.visibility = GONE
                            results.clear()
                            results.addAll(foundTreks)
                            trackList.visibility = VISIBLE
                            adapter.notifyDataSetChanged()
                            if (results.isEmpty() && binding.editText.text.isNotEmpty()) {

                                showMessage(
                                    getString(R.string.error_is_empty),
                                    "",
                                    R.drawable.search_error_mode
                                )
                            } else {
                                showMessage("", "1", R.drawable.intent_mode)
                                progressBar.visibility = GONE
                            }
                        }

                    }
                })
        }

    }


    private fun showMessage(text: String, additionalMessage: String, drawable: Int) {

        placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0)

        if (text.isNotEmpty()) {
            placeholderMessage.visibility = VISIBLE
            results.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            buttonUpdate.visibility = GONE
            if (additionalMessage.isNotEmpty()) {
                buttonUpdate.visibility = VISIBLE
            }
        } else {
            placeholderMessage.visibility = GONE
            buttonUpdate.visibility = GONE
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

    private fun viewGroupTrackList2(visible: Int) {
        trackList2.visibility = visible
        textFind.visibility = visible
        clearButtonHistory.visibility = visible
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
