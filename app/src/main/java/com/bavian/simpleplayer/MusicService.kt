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

        val list = LocalCompositionsList(intent!!.extras!!.getStringArray("paths")!!)

        if (player == null) {
            player = Player(list)
            player!!.play(0)
        } else {
            player!!.compositions = list
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