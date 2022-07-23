package com.bavian.simpleplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView

class PlayerInterface : AppCompatActivity() {

    private var timerHandler: Handler? = null
    private var timerUpdater: Runnable? = null
    private var hasStopped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paths = intent?.extras?.getStringArray("paths")

        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("paths", paths)
        startService(intent)

        setContentView(R.layout.player)

        if (MusicService.player?.isPlaying() != true && savedInstanceState != null) {
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

            if (MusicService.player?.isPlaying() != true) {
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

        if (MusicService.player!!.isPlaying()) {

            button.setImageResource(R.drawable.ic_play_48)
            MusicService.player!!.pause()

        } else {

            button.setImageResource(R.drawable.ic_pause_48)
            MusicService.player!!.play()

        }

    }

    fun next(view: View) = MusicService.player!!.next()
    fun previous(view: View) = MusicService.player!!.previous()

    private fun launchSeekBarAnalyser() {

        val seekBar = findViewById<SeekBar>(R.id.timer)
        val leftTimer = findViewById<TextView>(R.id.current_time)
        val rightTimer = findViewById<TextView>(R.id.rest_time)

        val name = findViewById<TextView>(R.id.name)
        val author = findViewById<TextView>(R.id.author)

        val timerHandler = Handler()
        val timerUpdater = object : Runnable {

            override fun run() {
                seekBar.max = MusicService.player?.duration ?: 0
                val progress = MusicService.player?.progress
                val duration = MusicService.player?.duration

                val restTime = "-${getTimer(duration?.minus(progress ?: 0))}"

                seekBar.progress = progress ?: 0
                leftTimer.text = getTimer(progress)
                rightTimer.text = restTime

                name.text = MusicService.player?.currentCompositionName ?: "Unknown"
                author.text = MusicService.player?.currentCompositionAuthor ?: "Anonymous"

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
                    MusicService.player?.progress = progress
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
