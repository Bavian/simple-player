package com.bavian.simpleplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.bavian.player.MusicPlayer
import org.koin.android.ext.android.inject

class PlayerInterface : AppCompatActivity() {

    private var hasStopped = false

    private val musicPlayer: MusicPlayer by inject()

    private val seekBarView: SeekBar by lazy { findViewById(R.id.timer) }
    private val leftTimerView: TextView by lazy { findViewById(R.id.current_time) }
    private val rightTimerView: TextView by lazy { findViewById(R.id.rest_time) }

    private val nameView: TextView by lazy { findViewById(R.id.name) }
    private val authorView: TextView by lazy { findViewById(R.id.author) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val paths = intent?.extras?.getStringArray("paths")

        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("paths", paths)
        startService(intent)

        setContentView(R.layout.player)

        if (!musicPlayer.isPlaying() && savedInstanceState != null) {
            findViewById<ImageButton>(R.id.play).setImageResource(R.drawable.ic_play_48)
        }

        launchGestureListener()

    }

    override fun onStart() {
        super.onStart()

        launchSeekBarListener()

        musicPlayer.track.observe(this) {
            nameView.text = it.title ?: "Unknown"
            authorView.text = it.author ?: "Anonymous"
            rightTimerView.text = getTimer(it.duration)
            seekBarView.max = it.duration.toInt()
        }

        musicPlayer.currentTime.observe(this) {
            leftTimerView.text = getTimer(it)
            seekBarView.progress = it.toInt()
        }

        if (hasStopped) {

            val button = findViewById<ImageButton>(R.id.play)

            if (!musicPlayer.isPlaying()) {
                button.setImageResource(R.drawable.ic_play_48)
            } else {
                button.setImageResource(R.drawable.ic_pause_48)
            }

        }

        hasStopped = false

    }

    fun play(view: View) {

        val button = findViewById<ImageButton>(R.id.play)

        if (musicPlayer.isPlaying()) {

            button.setImageResource(R.drawable.ic_play_48)
            musicPlayer.pause()

        } else {

            button.setImageResource(R.drawable.ic_pause_48)
            musicPlayer.play()

        }

    }

    fun next(view: View) = musicPlayer.next()
    fun previous(view: View) = musicPlayer.previous()

    private fun launchSeekBarListener() {
        val seekBar = findViewById<SeekBar>(R.id.timer)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicPlayer.seek(progress.toLong())
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
