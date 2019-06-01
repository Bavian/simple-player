package com.bavian.simpleplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bavian.simpleplayer.player.Player
import com.bavian.simpleplayer.player.compositions.LocalCompositionsList

class MusicService: Service() {

    companion object {
        var player: Player? = null
            private set
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val result = super.onStartCommand(intent, flags, startId)

        if (player == null) {
            player = Player(LocalCompositionsList(intent!!.extras!!.getStringArray("paths")!!))
            player!!.play(0)
        }

        return result
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.off()
    }

}