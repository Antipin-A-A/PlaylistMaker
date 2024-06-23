package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.button_search_clean)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
        }

        buttonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {

            }

            override fun onTextChanged(s : CharSequence?, start : Int, before : Int, coutn : Int) {
               if (!s.isNullOrEmpty()){
                   clearButton.visibility = View.VISIBLE
               }else{
                   clearButton.visibility = View.GONE
               }

            }

            override fun afterTextChanged(p0 : Editable?) {

            }

        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
}