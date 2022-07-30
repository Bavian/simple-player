package com.bavian.player

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.TimeUnit

class TimerRunnable(private val mainHandler: Handler) : Runnable {

    private val delay = TimeUnit.SECONDS.toMillis(1)

    var counter = MutableLiveData<Long>(0)

    override fun run() {
        counter.value = (counter.value ?: 0) + delay
        repost()
    }

    fun start(position: Long = 0) {
        counter.value = position
        repost()
    }

    fun stop() {
        mainHandler.removeCallbacks(this)
    }

    private fun repost() {
        mainHandler.removeCallbacks(this)
        mainHandler.postDelayed(this, delay)
    }
}