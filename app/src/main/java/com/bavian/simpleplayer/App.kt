package com.bavian.simpleplayer

import android.app.Application
import com.bavian.player.musicMusicPlayerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                musicMusicPlayerModule,
                HandlersModule.handlersModule,
            )
        }
    }

}
