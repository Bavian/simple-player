package com.bavian.simpleplayer.player

import android.media.MediaPlayer
import com.bavian.simpleplayer.player.compositions.CompositionsList
import kotlin.random.Random

class Player(private var compositions: CompositionsList) {

    private val mediaPlayer = MediaPlayer()
    private var current: Int = 0
    private var isShuffle = false
    private var isRepeat = false

    init {
        synchronized(mediaPlayer) {
            mediaPlayer.setOnPreparedListener { mp -> mp.start() }
            mediaPlayer.setOnCompletionListener {
                run {

                    if (isShuffle) {
                        play(Random.nextInt(0, compositions.size))
                    } else {

                        if (current + 1 == compositions.size && !isRepeat) {
                            pause()
                        } else {
                            next()
                        }

                    }

                }
            }
        }
    }

    public fun play(index: Int) {

        var i = index

        if (index !in 0..compositions.size) {
            i = (compositions.size + index) % compositions.size
        }

        synchronized(mediaPlayer) {
            mediaPlayer.setDataSource(compositions.get(i))
        }
    }

    public fun next() {
        play(current + 1)
    }

    public fun previous() {
        play(current - 1)
    }

    public fun play() {
        synchronized(mediaPlayer) {
            mediaPlayer.start()
        }
    }

    public fun pause() {
        synchronized(mediaPlayer) {
            mediaPlayer.pause()
        }
    }

    public var progress: Float
        get() {
            return mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration
        }
        set(percent: Float) {
            synchronized(mediaPlayer) {
                mediaPlayer.seekTo((percent * mediaPlayer.duration).toInt())
            }
        }

    public fun off() {
        synchronized(mediaPlayer) {
            mediaPlayer.release()
        }
    }

}