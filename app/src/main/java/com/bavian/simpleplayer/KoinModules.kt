package com.bavian.simpleplayer

import com.bavian.simpleplayer.player.Player
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object KoinModules {
    val musicPlayerModule = module {
        singleOf(::Player)
    }
}