package com.example.playlistmaker

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private var statusString : String = INPUTTEXT

    private val results = ArrayList<Track>()
    private val adapter = MusicAdapter()
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.button_back_Search)
        val editText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.button_search_clean)
        val trackList = findViewById<RecyclerView>(R.id.music_list)
        buttonUpdate = findViewById(R.id.button_upDate)


        adapter.tracks = results
        trackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackList.adapter = adapter

        clearButton.setOnClickListener {
            editText.setText("")
            val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            results.clear()
        }

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {

            }

            override fun onTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {
                if (!p0.isNullOrEmpty()) {
                    clearButton.visibility = View.VISIBLE
                } else {
                    clearButton.visibility = View.GONE
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

    }

    private fun iTunesServiceSearch(string : String){
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
        placeholderMessage.setCompoundDrawablesWithIntrinsicBounds(0,drawable,0,0)

        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            results.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            buttonUpdate.visibility = View.GONE
            if (additionalMessage.isNotEmpty()) {
                buttonUpdate.visibility = View.VISIBLE
            }
        } else {
            placeholderMessage.visibility = View.GONE
            buttonUpdate.visibility = View.GONE
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


    companion object {
        const val KEY_STRING = "KEY_STRING"
        const val INPUTTEXT = ""
    }
}