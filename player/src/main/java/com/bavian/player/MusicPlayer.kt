package com.bavian.player

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Tracks
import java.io.File

class MusicPlayer (
    private val exoPlayer: ExoPlayer,
    private val timer: TimerRunnable,
) {

    private val exoPlayerListener = object : Player.Listener {
        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            val mediaMetadata = exoPlayer.mediaMetadata
            track.value = Track(
                mediaMetadata.title,
                mediaMetadata.artist,
                exoPlayer.duration,
            )
            timer.start(0)
        }
    }

    init {
        exoPlayer.addListener(exoPlayerListener)
    }

    val track = MutableLiveData(Track(null, null,0))
    val currentTime = timer.counter

    fun play() {
        timer.start()
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun resume() {
        timer.start(exoPlayer.currentPosition)
        exoPlayer.play()
    }

    fun pause() {
        timer.stop()
        exoPlayer.pause()
    }

    fun seek(position: Long) {
        timer.stop()
        timer.start(position)
        exoPlayer.seekTo(position)
    }

    fun stop() {
        timer.stop()
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

    fun isPlaying() = exoPlayer.isPlaying

    fun next() {
        exoPlayer.seekToNextMediaItem()
    }

    fun previous() {
        exoPlayer.seekToPreviousMediaItem()
    }

    fun addTrack(file: File, index: Int = exoPlayer.mediaItemCount) {
        exoPlayer.addMediaItem(index, MediaItem.fromUri(file.toUri()))
    }

    fun addTrack(path: String, index: Int = exoPlayer.mediaItemCount) {
        addTrack(File(path), index)
    }

    fun removeTrack(index: Int) {
        exoPlayer.removeMediaItem(index)
    }

}