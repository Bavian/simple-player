package com.bavian.simpleplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

const val REST_TIME_UPDATE_DELAY_MILLIS = 1000L

class StartActivity : AppCompatActivity() {

    private lateinit var miniPlayer: View
    private lateinit var trackNameTextView: TextView
    private lateinit var restTimeTextView: TextView

    private val mainHandler = Handler(Looper.getMainLooper())
    private val updateRestTimeCached = this::updateRestTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        findViewById<View>(R.id.choose_directory_button).setOnClickListener {
            chooseDirectory()
        }

        miniPlayer = findViewById(R.id.mini_player)
        restTimeTextView = miniPlayer.findViewById(R.id.mini_player_rest_time_text_view)
        trackNameTextView = miniPlayer.findViewById(R.id.mini_player_track_name_text_view)
        findViewById<View>(R.id.mini_player_pause_button).setOnClickListener {
            pauseTrack()
        }
        findViewById<View>(R.id.mini_player_skip_next_button).setOnClickListener {
            launchNextTrack()
        }
    }

    override fun onResume() {
        super.onResume()
        if (MusicService.player?.isPlaying() == true) {
            miniPlayer.visibility = View.VISIBLE
            updateRestTime()
            mainHandler.postDelayed(updateRestTimeCached, REST_TIME_UPDATE_DELAY_MILLIS)
        } else {
            miniPlayer.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        mainHandler.removeCallbacks(updateRestTimeCached)
    }

    private fun chooseDirectory() {
        val intent = Intent(this, DirectoryChooser::class.java)
        startActivity(intent)
    }

    private fun pauseTrack() {
        MusicService.player?.pause()
        findViewById<LinearLayout>(R.id.mini_player).visibility = View.GONE
    }

    private fun launchNextTrack() {
        MusicService.player?.next()
    }

    private fun updateRestTime() {
        val progress = MusicService.player?.progress
        val duration = MusicService.player?.duration
        val restTime = "-" + getTimer(duration?.minus(progress ?: 0))

        restTimeTextView.text = restTime
        trackNameTextView.text = MusicService.player?.currentCompositionName ?: getString(R.string.mini_player_unknown_track_name)
    }
}
