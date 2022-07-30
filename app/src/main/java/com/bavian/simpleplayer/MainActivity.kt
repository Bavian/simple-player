package com.bavian.simpleplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bavian.player.Player
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var timerHandler: Handler? = null
    private var timerUpdater: Runnable? = null

    private val player: Player by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchGestureListener()
    }

    override fun onStart() {
        super.onStart()

        val miniPlayer = findViewById<LinearLayout>(R.id.mini_player)

        if (player.isPlaying()) {
            miniPlayer.visibility = View.VISIBLE
        } else {
            miniPlayer.visibility = View.GONE
        }

        launchCompositionAnalyser()
    }

    override fun onStop() {
        super.onStop()
        timerHandler?.removeCallbacks(timerUpdater!!)
    }

    fun chooseDirectory(view: View) {
        val intent = Intent(this, DirectoryChooser::class.java)
        startActivity(intent)
    }

    fun pause(view: View) {
        player.pause()
        findViewById<LinearLayout>(R.id.mini_player).visibility = View.GONE
    }

    fun next(view: View) {
        player.next()
    }

    private fun launchCompositionAnalyser() {

        val timer = findViewById<TextView>(R.id.rest_time)

        val name = findViewById<TextView>(R.id.name)

        val timerHandler = Handler()
        val timerUpdater = object : Runnable {

            override fun run() {
                val progress = player.progress
                val duration = player.duration

                val restTime = "-${getTimer(duration?.minus(progress ?: 0))}"
                timer.text = restTime

                name.text = player.currentCompositionName ?: "Unknown"

                timerHandler.postDelayed(this, 50)
            }

        }

        timerHandler.postDelayed(timerUpdater, 0)

        this.timerHandler = timerHandler
        this.timerUpdater = timerUpdater
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
