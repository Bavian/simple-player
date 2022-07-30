package com.bavian.simpleplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.bavian.player.Player
import org.koin.android.ext.android.inject

class PlayerInterface : AppCompatActivity() {

    private var timerHandler: Handler? = null
    private var timerUpdater: Runnable? = null
    private var hasStopped = false

    private val player: Player by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paths = intent?.extras?.getStringArray("paths")

        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("paths", paths)
        startService(intent)

        setContentView(R.layout.player)

        if (!player.isPlaying() && savedInstanceState != null) {
            findViewById<ImageButton>(R.id.play).setImageResource(R.drawable.ic_play_48)
        }

        launchGestureListener()

    }

    override fun onStart() {
        super.onStart()

        launchSeekBarAnalyser()
        launchSeekBarListener()

        if (hasStopped) {

            val button = findViewById<ImageButton>(R.id.play)

            if (!player.isPlaying()) {
                button.setImageResource(R.drawable.ic_play_48)
            } else {
                button.setImageResource(R.drawable.ic_pause_48)
            }

        }

        hasStopped = false

    }

    override fun onStop() {
        super.onStop()
        hasStopped = true
        timerHandler?.removeCallbacks(timerUpdater!!)
    }

    fun play(view: View) {

        val button = findViewById<ImageButton>(R.id.play)

        if (player.isPlaying()) {

            button.setImageResource(R.drawable.ic_play_48)
            player.pause()

        } else {

            button.setImageResource(R.drawable.ic_pause_48)
            player.play()

        }

    }

    fun next(view: View) = player.next()
    fun previous(view: View) = player.previous()

    private fun launchSeekBarAnalyser() {

        val seekBar = findViewById<SeekBar>(R.id.timer)
        val leftTimer = findViewById<TextView>(R.id.current_time)
        val rightTimer = findViewById<TextView>(R.id.rest_time)

        val name = findViewById<TextView>(R.id.name)
        val author = findViewById<TextView>(R.id.author)

        val timerHandler = Handler()
        val timerUpdater = object : Runnable {

            override fun run() {
                seekBar.max = player.duration
                val progress = player.progress
                val duration = player.duration

                val restTime = "-${getTimer(duration.minus(progress))}"

                seekBar.progress = progress ?: 0
                leftTimer.text = getTimer(progress)
                rightTimer.text = restTime

                name.text = player.currentCompositionName ?: "Unknown"
                author.text = player.currentCompositionAuthor ?: "Anonymous"

                timerHandler.postDelayed(this, 50)
            }

        }

        timerHandler.postDelayed(timerUpdater, 0)

        this.timerHandler = timerHandler
        this.timerUpdater = timerUpdater

    }

    private fun launchSeekBarListener() {
        val seekBar = findViewById<SeekBar>(R.id.timer)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.progress = progress
                }
            }

        })
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
