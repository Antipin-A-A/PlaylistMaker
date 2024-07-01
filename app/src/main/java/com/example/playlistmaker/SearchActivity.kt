package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private var statusString : String = INPUTTEXT

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<TextView>(R.id.button_back_Search)
        val editText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.button_search_clean)



        clearButton.setOnClickListener {
            editText.setText("")
            val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
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
    }

    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STRING, statusString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        statusString = savedInstanceState.getString(KEY_STRING).toString()
        findViewById<EditText>(R.id.inputEditText).setText(statusString)
    }

    companion object {
        const val KEY_STRING = "KEY_STRING"
        const val INPUTTEXT = ""
    }
}