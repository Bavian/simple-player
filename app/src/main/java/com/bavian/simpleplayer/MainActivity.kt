package com.bavian.simpleplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bavian.player.MusicPlayer
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val musicPlayer: MusicPlayer by inject()

    private val timerView: TextView by lazy { findViewById(R.id.rest_time) }
    private val nameView: TextView by lazy { findViewById(R.id.name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicPlayer.track.observe(this) {
            timerView.text = getTimer(it.duration)
            nameView.text = it.title ?: "Unknown"
        }
    }

    override fun onStart() {
        super.onStart()

        val miniPlayer = findViewById<LinearLayout>(R.id.mini_player)

        if (musicPlayer.isPlaying()) {
            miniPlayer.visibility = View.VISIBLE
        } else {
            miniPlayer.visibility = View.GONE
        }
    }

    fun chooseDirectory(view: View) {
        val intent = Intent(this, DirectoryChooser::class.java)
        startActivity(intent)
    }

    fun pause(view: View) {
        musicPlayer.pause()
        findViewById<LinearLayout>(R.id.mini_player).visibility = View.GONE
    }

    fun next(view: View) {
        musicPlayer.next()
    }

    private fun launchGestureListener() {
        val gdt = GestureDetector(this, GestureListener { moveTaskToBack(true) })

        val touchingView = findViewById<View>(R.id.main)
        touchingView.setOnTouchListener { _, event ->
            gdt.onTouchEvent(event)
            true
        }
    }

}
