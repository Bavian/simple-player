package com.bavian.simpleplayer

import android.os.Handler
import android.os.Looper
import org.koin.core.qualifier.named
import org.koin.dsl.module

object HandlersModule {

    const val MAIN_HANDLER = "main_handler"

    val handlersModule = module {
        single(named(MAIN_HANDLER)) { Handler(Looper.getMainLooper()) }
    }
}