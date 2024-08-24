package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var statusString : String = INPUT_TEXT
    private var items = mutableListOf<Track>()
    private val results = mutableListOf<Track>()
    lateinit var adapter : MusicAdapter
    lateinit var adapter2 : MusicAdapter
    private lateinit var buttonUpdate : Button
    private lateinit var trackList2 : RecyclerView
    private lateinit var textFind : TextView
    private lateinit var clearButtonHistory : Button

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBackToollbar = findViewById<Toolbar>(R.id.toolbar)
        val editText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.button_search_clean)
        val trackList = findViewById<RecyclerView>(R.id.music_list)
        buttonUpdate = findViewById(R.id.buttonUpDate)
        trackList2 = findViewById(R.id.music_list2)
        textFind = findViewById(R.id.textLookingForYou)
        clearButtonHistory = findViewById(R.id.buttonClearHistory)

        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val facts = sharedPreferences.getString(FACTS_LIST_KEY, null)

        if (facts != null) {
            items = createFactsListFromJson(facts)
        }
        val onItemClickListener = OnItemClickListener {
            val itemsTrack = ItemsTrack()
            val track = Track(
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.trackId,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country
            )
            itemsTrack.itemsListTrack(items, it)
            sharedPreferences.edit()
                .putString(FACTS_LIST_KEY, createJsonFromFactsList(items))
                .putString(NEW_FACT_KEY, createJsonFromFact(track))
                .apply()
            startActivity(Intent(this,MusicActivity::class.java))
        }

        adapter2 = MusicAdapter(onItemClickListener)
        adapter2.tracks = items
        trackList2.adapter = adapter2

        adapter = MusicAdapter(onItemClickListener)
        adapter.tracks = results
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = adapter

        clearButton.setOnClickListener {
            editText.setText("")
            val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            results.clear()
            adapter.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
        }

        buttonBackToollbar.setNavigationOnClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && editText.text.isEmpty() && items.isNotEmpty()) {
                viewGroupTrackList2(VISIBLE)
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {

            }

            override fun onTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
                if (editText.hasFocus() && p0?.isEmpty() == true && items.isNotEmpty()) {
                    viewGroupTrackList2(VISIBLE)
                } else {
                    viewGroupTrackList2(GONE)
                }

                if (!p0.isNullOrEmpty()) {
                    clearButton.visibility = VISIBLE
                } else {
                    clearButton.visibility = GONE
                }
            }

            override fun afterTextChanged(p0 : Editable?) {
                statusString = editText.text.toString()
            }

        }
        editText.addTextChangedListener(simpleTextWatcher)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                iTunesServiceSearch(editText.text.toString())
                true
            }
            false
        }

        buttonUpdate.setOnClickListener {
            iTunesServiceSearch(editText.text.toString())
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

    private fun iTunesServiceSearch(string : String) {
        iTunesService.search(string)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call : Call<TrackResponse>,
                    response : Response<TrackResponse>,
                ) {
                    if (response.code() == 200) {
                        results.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            results.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (results.isEmpty()) {
                            showMessage(
                                getString(R.string.error_is_empty),
                                "",
                                R.drawable.search_error_mode
                            )
                        } else {
                            showMessage("", "1", R.drawable.intent_mode)
                        }
                    } else {
                        showMessage(
                            getString(R.string.errorWifi),
                            response.code().toString(),
                            R.drawable.intent_mode
                        )
                    }
                }

                override fun onFailure(call : Call<TrackResponse>, t : Throwable) {
                    showMessage(
                        getString(R.string.errorWifi),
                        t.message.toString(),
                        R.drawable.intent_mode
                    )
                }
            })
    }

    private fun showMessage(text : String, additionalMessage : String, drawable : Int) {
        val placeholderMessage = findViewById<TextView>(R.id.placeholderMessage)
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

    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STRING, statusString)
    }

    override fun onRestoreInstanceState(savedInstanceState : Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        statusString = savedInstanceState.getString(KEY_STRING) ?: ""
        findViewById<EditText>(R.id.inputEditText).setText(statusString)
    }

    private fun createFactsListFromJson(json : String?) : ArrayList<Track> {
        val itemType = object : TypeToken<ArrayList<Track?>?>() {}.type
        val itemList = Gson().fromJson<ArrayList<Track>>(json, itemType)
        return itemList
    }

    private fun createJsonFromFactsList(facts : MutableList<Track>) : String {
        return Gson().toJson(facts)
    }
    private fun createJsonFromFact(fact : Track) : String {
        return Gson().toJson(fact)
    }

    private fun viewGroupTrackList2(visible : Int) {
        trackList2.visibility = visible
        textFind.visibility = visible
        clearButtonHistory.visibility = visible
    }

    companion object {
        const val KEY_STRING = "KEY_STRING"
        const val INPUT_TEXT = ""
        const val FACTS_LIST_KEY = "FACTS_LIST_KEY"
    }
}
