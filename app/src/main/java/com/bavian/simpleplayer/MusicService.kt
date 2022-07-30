package com.bavian.simpleplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bavian.player.MusicPlayer
import org.koin.android.ext.android.inject

class MusicService: Service() {

    private val compositions = ArrayList<String>()
    private val musicPlayer: MusicPlayer by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)

        musicPlayer.stop()
        compositions.addAll(intent?.extras?.getStringArray("paths") ?: return result)
        intent.extras?.getStringArray("paths")?.forEach {
            musicPlayer.addTrack(it)
        }
        musicPlayer.play()

        return result
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
    }

}
