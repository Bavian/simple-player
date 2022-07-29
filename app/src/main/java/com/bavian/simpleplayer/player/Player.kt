package com.bavian.simpleplayer.player

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import kotlin.random.Random

class Player {

    private val mediaPlayer = MediaPlayer()
    private val data = MediaMetadataRetriever()
    private var current: Int = 0
    private var isShuffle = false
    private var isRepeat = false

    var duration: Int = 0
        private set

    var compositions = ArrayList<String>()
        set(value) {
            if (compositions != value) {
                field = value
                play(0)
            }
        }

    init {

        mediaPlayer.setOnPreparedListener { mp ->
            mp.start()
            duration = mp.duration
        }

        mediaPlayer.setOnCompletionListener {

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

    fun play(index: Int) {
        if (compositions.isEmpty()) return

        current = (compositions.size + index) % compositions.size

        mediaPlayer.reset()
        mediaPlayer.setDataSource(compositions.get(current))
        mediaPlayer.prepareAsync()

        data.setDataSource(compositions.get(current))
    }

    fun next() {
        play(current + 1)
    }

    fun previous() {
        play(current - 1)
    }

    fun play() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun isPlaying() = mediaPlayer.isPlaying

    var progress: Int
        get() = mediaPlayer.currentPosition
        set(toSet) {
            mediaPlayer.seekTo(toSet)
        }

    fun off() {
        mediaPlayer.release()
    }

    val currentCompositionName: String?
        get() = data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

    val currentCompositionAuthor: String?
        get() = data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
}