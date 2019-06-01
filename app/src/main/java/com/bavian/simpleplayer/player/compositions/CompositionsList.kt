package com.bavian.simpleplayer.player.compositions

interface CompositionsList: Comparable<CompositionsList> {
    fun get(index: Int): String
    val size: Int
}