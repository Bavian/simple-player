package com.bavian.simpleplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log

class Player : Service() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
        var isWaitingForStart: Boolean = false
        var timeToSeek: Int = 0

        fun pause() {
            if (mediaPlayer != null) {
                mediaPlayer!!.pause()
            } else {
                isWaitingForStart = false
            }
        }

        fun start() {
            if (mediaPlayer != null) {
                mediaPlayer!!.start()
            } else {
                isWaitingForStart = true
            }
        }

        fun isPlaying(): Boolean {
            return if (mediaPlayer == null) false else mediaPlayer!!.isPlaying
        }

        fun seekTo(time: Int) {
            if (mediaPlayer != null) {
                Log.d("Fuck", "normalSeek: $time")
                mediaPlayer!!.seekTo(time)
            } else {
                Log.d("Fuck", "timeToSeek: $time")
                timeToSeek = time
            }
        }

        fun getDuration(): Int {
            return if (mediaPlayer != null) {
                mediaPlayer!!.duration
            } else {
                0
            }
        }

        fun getProgress(): Int {
            return if (mediaPlayer != null) {
                mediaPlayer!!.currentPosition;
            } else {
                0
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)

        val extras = intent!!.extras
        val compositionPath = extras.getString("composition")

        mediaPlayer = MediaPlayer.create(this, Uri.parse("file:///$compositionPath"))

        if (isWaitingForStart) {
            mediaPlayer!!.start()
        }

        Log.d("Fuck", "Let's seek: $timeToSeek")
        mediaPlayer!!.seekTo(timeToSeek)
        timeToSeek = 0

        val mp = mediaPlayer
        mp!!.start()

        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}