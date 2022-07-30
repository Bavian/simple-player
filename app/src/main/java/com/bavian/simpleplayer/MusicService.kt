package com.bavian.simpleplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bavian.player.Player
import org.koin.android.ext.android.inject

class MusicService: Service() {

    private val compositions = ArrayList<String>()
    private val player: Player by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)

        compositions.addAll(intent?.extras?.getStringArray("paths") ?: return result)
        player.compositions = compositions

        return result
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        player.off()
    }

}
