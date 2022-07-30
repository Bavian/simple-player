package com.bavian.player

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val musicPlayerModule = module {
    singleOf(::Player)
}