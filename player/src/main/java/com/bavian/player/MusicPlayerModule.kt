package com.bavian.player

import com.google.android.exoplayer2.ExoPlayer
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val musicMusicPlayerModule = module {
    single { ExoPlayer.Builder(get()).build() }
    single { TimerRunnable(get(named("main_handler"))) }
    singleOf(::MusicPlayer)
}