package com.bavian.simpleplayer

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.GestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import java.io.File
import android.widget.TextView
import android.view.View.OnTouchListener
import android.widget.Toast


class PlayerInterface : AppCompatActivity() {

    companion object {
        private var compositionNumber : Int = 0
    }

    private var serviceIntent : Intent? = null
    private var compositionsPaths : ArrayList<String>? = null

    private var timerHandler: Handler? = null
    private var timerUpdater: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player)

        val extras = intent.extras

        val directory = File(extras.getString("path"))
        val paths = ArrayList<String>()

        for (file in directory.listFiles()) {

            if (getFileExtension(file).equals(".mp3")) {
                paths.add(file.absolutePath)
            }

        }

        if (paths.size == 0) {

            Toast.makeText(applicationContext, "В данной директории нет .mp3 файлов", Toast.LENGTH_SHORT).show()

            onBackPressed()
            return
        }

        compositionsPaths = paths

        val time = savedInstanceState?.getInt("time", 0) ?: 0
        turnOn(compositionNumber, true, time)

        val seekBar = findViewById<SeekBar>(R.id.timer)
        val leftTimer = findViewById<TextView>(R.id.current_time)
        val rightTimer = findViewById<TextView>(R.id.rest_time)

        val timerHandler = Handler()
        val timerUpdater = object : Runnable {

            override fun run() {
                seekBar.max = Player.getDuration()

                val progress = Player.getProgress()
                val duration = Player.getDuration()

                val restTime = "-${getTimer(duration - progress)}"

                seekBar.progress = progress
                leftTimer.text = getTimer(progress)
                rightTimer.text = restTime

                if (progress >= duration) {
                    next(null)
                }

                timerHandler.postDelayed(this, 1000)
            }

        }

        this.timerHandler = timerHandler
        this.timerUpdater = timerUpdater

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    Player.seekTo(seekBar!!.progress)
                }
            }

        })

        val gdt = GestureDetector(GestureListener(fun() {
            moveTaskToBack(true)
        }))

        val touchingView = findViewById<View>(R.id.main)
        touchingView.setOnTouchListener(OnTouchListener { _, event ->
            gdt.onTouchEvent(event)
            true
        })
    }

    override fun onStart() {
        super.onStart()
        timerHandler!!.postDelayed(timerUpdater, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("time", min(Player.getProgress() + 250, Player.getDuration()))
    }

    private fun turnOff() {
        Player.pause()
        findViewById<ImageButton>(R.id.play).setImageResource(R.drawable.ic_play_48)
    }

    private fun turnOn(number: Int, isNew: Boolean = false, time: Int = 0) {
        compositionNumber = number

        if (isNew) {

            turnOnPlayer(compositionsPaths!![number], time)

        } else {
            Player.start()
        }

        findViewById<ImageButton>(R.id.play).setImageResource(R.drawable.ic_pause_48)

    }

    private fun turnOnPlayer(composition: String, time: Int) {

        if (serviceIntent != null) {
            stopService(serviceIntent)
        }

        val newIntent = Intent(this, Player::class.java)
        newIntent.addCategory("player")
        newIntent.putExtra("composition", composition)

        Player.timeToSeek = time

        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(composition)

        findViewById<TextView>(R.id.name).text = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        findViewById<TextView>(R.id.author).text = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

        stopService(newIntent)
        startService(newIntent)
        serviceIntent = newIntent
    }

    fun play(view: View) {

        if(serviceIntent == null) {
            turnOn(compositionNumber, true)
        }

        if (Player.isPlaying()) {
            turnOff()
        } else {
            turnOn(compositionNumber)
        }

    }

    fun next(view: View?) {
        turnOn((compositionNumber + 1) % compositionsPaths!!.size, true)
    }

    fun previous(view: View) {
        turnOn((compositionsPaths!!.size + compositionNumber - 1) % compositionsPaths!!.size, true)
    }

    override fun onStop() {
        super.onStop()
        timerHandler!!.removeCallbacks(timerUpdater)
    }

}

fun getFileExtension(file: File): String {

    val name = file.name
    val lastIndexOf = name.lastIndexOf(".")

    return if (lastIndexOf == -1) {
        ""
    } else {
        name.substring(lastIndexOf)
    }

}

fun getTimer(time: Int): String {
    val seconds = time / 1000 % 60
    val minutes = time / 1000 / 60

    val secondsString = if (seconds < 10) "0$seconds" else seconds.toString()

    return "$minutes:$secondsString"
}

fun min(a: Int, b: Int): Int {
    return if (a < b) a else b
}