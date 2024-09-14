package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView


class Player(
    private val url: String,
    private val playButtonView: ImageView,
    val trackTimeView: TextView,
    val handler: Handler?
) {
    var isRunTime = false
    var mediaPlayer = MediaPlayer()
    var secondsCount = 31L

    fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButtonView.setImageResource(R.drawable.play_icon)
            playerState = STATE_PREPARED
            secondsCount = 31L
            isRunTime = false
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        isRunTime = true

        println("isRunTime = $isRunTime")
        playButtonView.setImageResource(R.drawable.pause_icon)
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        isRunTime = false
        println("isRunTime = $isRunTime")
        playButtonView.setImageResource(R.drawable.play_icon)
        playerState = STATE_PAUSED
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                createUpdateTimerTask()
//                createUpdateTimerTask2()
            }
        }
    }

    /*Какой счетчи правильно использовать работают оба */
    /*Счетчик-1*/
    private fun createUpdateTimerTask() {
        var startTime = System.currentTimeMillis()
        var seconds = 0L
        handler?.post(
            object : Runnable {
                override fun run() {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val remainingTime = (secondsCount * DELAY) - elapsedTime
                    if (isRunTime && remainingTime > 0) {
                        seconds = remainingTime / DELAY
                        trackTimeView?.text =
                            String.format("%d:%02d", seconds / 60, seconds % 60)
                        handler?.postDelayed(this, DELAY)
                    } else {
                        secondsCount = seconds
                    }
                }
            })
    }
    /*Счетчик-2*/
    private fun createUpdateTimerTask2() {
        var second = 1L
        handler?.post(
            object : Runnable {
                override fun run() {
                    println("second -$second")
                    if (isRunTime && second > 0) {
                        second = secondsCount - 1
                        secondsCount = second
                        trackTimeView?.text =
                            String.format("%d:%02d", second / 60, second % 60)
                        handler?.postDelayed(this, DELAY)
                    }else{
                        secondsCount = second+1
                    }

                }
            })


    }

    companion object {
        /*четыре константы для каждого из состояния mediaPlay*/
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
}