package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    private var statusString: String = INPUT_TEXT
    private var items = mutableListOf<Track>()
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

        buttonUpdate = findViewById(R.id.buttonUpDate)
        trackList2 = findViewById(R.id.trackList2)
        textFind = findViewById(R.id.textLookingForYou)
        clearButtonHistory = findViewById(R.id.buttonClearHistory)
        placeholderMessage = findViewById(R.id.placeholderMessage)

        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val facts = sharedPreferences.getString(FACTS_LIST_KEY, null)

        if (facts != null) {
            items = createFactsListFromJson(facts)
        }
        val onItemClickListener = OnItemClickListener { track ->
            if (clickDebounce()) {
                val itemsTrack = ItemsTrack()
                itemsTrack.itemsListTrack(items, track)
                sharedPreferences.edit()
                    .putString(FACTS_LIST_KEY, createJsonFromFactsList(items))
                    .apply()

                val intent = Intent(this, MusicActivity::class.java)
                intent.putExtra(NEW_FACT_KEY, createJsonFromFact(track))
                startActivity(intent)
            }
        }

        adapter2 = MusicAdapter(onItemClickListener)
        adapter2.tracks = items
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
            if (items.isNotEmpty()) viewGroupTrackList2(VISIBLE)
        }

        binding.toolbar.setNavigationOnClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editText.text.isEmpty() && items.isNotEmpty()) {
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
            sharedPreferences.edit()
                .remove(FACTS_LIST_KEY)
                .apply()
            items.clear()
            adapter2.notifyDataSetChanged()
            viewGroupTrackList2(GONE)
        }
    }

    private fun iTunesServiceSearch() = with(binding) {

        viewGroupTrackList2(VISIBLE)
        adapter2.notifyDataSetChanged()
        trackList.visibility = GONE

        if (editText.hasFocus() && editText.text.isNotEmpty()) {
            viewGroupTrackList2(GONE)
            placeholderMessage.visibility = GONE
            progressBar.visibility = VISIBLE
            buttonCleanSearch.visibility = VISIBLE
            progressBar.visibility = GONE
            buttonUpdate.visibility = GONE

            iTunesService.search(editText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>,
                    ) {
                        progressBar.visibility = GONE
                        if (response.code() == 200) {
                            results.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.visibility = VISIBLE
                                results.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }

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
                        } else {
                            progressBar.visibility = GONE
                            showMessage(
                                getString(R.string.errorWifi),
                                response.code().toString(),
                                R.drawable.intent_mode
                            )
                        }
                    }


                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        viewGroupTrackList2(GONE)
                        showMessage(
                            getString(R.string.errorWifi),
                            t.message.toString(),
                            R.drawable.intent_mode
                        )
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

    private fun createFactsListFromJson(json: String?): ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track?>?>() {}.type
        val itemList = Gson().fromJson<ArrayList<Track>>(json, itemType)
        return itemList
    }

    private fun createJsonFromFactsList(facts: MutableList<Track>): String {
        return Gson().toJson(facts)
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
        const val FACTS_LIST_KEY = "FACTS_LIST_KEY"
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
