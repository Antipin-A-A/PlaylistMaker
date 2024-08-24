package com.example.playlistmaker

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val buttonBack = findViewById<Toolbar>(R.id.toolbar)
        val textTrackName = findViewById<TextView>(R.id.textViewArtistName)
        val textCollectionFirst = findViewById<TextView>(R.id.textViewCollectionNameFirst)
        val textCollection = findViewById<TextView>(R.id.textViewCollectionName)
        val trackTime = findViewById<TextView>(R.id.textViewTrackTime)
        val releaseDate = findViewById<TextView>(R.id.textViewReleaseDate)
        val primaryGenreName = findViewById<TextView>(R.id.textViewPrimaryGenreName)
        val country = findViewById<TextView>(R.id.textViewCountry)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val addButton = findViewById<ImageButton>(R.id.buttonAdd)

        buttonBack.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val sharedPreferences = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        val factTrack = sharedPreferences.getString(NEW_FACT_KEY, null)
        if (factTrack != null) {
            val it = createFactFromJson(factTrack)
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
            textTrackName.text = track.artistName
            textCollectionFirst.text = track.trackName
            textCollection.text = track.collectionName
            val time = track.trackTimeMillis
            val convectorTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toLong())

            val data = track.releaseDate
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val localDateTime = LocalDateTime.parse(data, pattern)

            trackTime.text = convectorTime
            releaseDate.text = localDateTime.year.toString()
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country

            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.image)
                .fitCenter()
                .transform(RoundedCorners(10))
                .into(imageView)

            addButton.setOnClickListener {
                snake(it,"Плейлист «BeSt SoNg EvEr!» создан")

            }
        }
    }

    private fun snake(view : View, string : String){
        val snack : Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG)
        snack.setTextColor(R.color.white_Black)
        snack.show()
    }
    private fun createFactFromJson(json : String) : Track {
        return Gson().fromJson(json, Track::class.java)
    }
}