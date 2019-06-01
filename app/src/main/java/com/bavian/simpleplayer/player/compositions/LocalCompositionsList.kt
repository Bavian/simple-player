package com.bavian.simpleplayer.player.compositions

class LocalCompositionsList(paths: Array<String>): CompositionsList {

    private val compositions = ArrayList<String>()

    init {
        compositions.addAll(paths)
    }

    override fun get(index: Int): String {
        return compositions[index]
    }

    override val size: Int
        get() {
            return compositions.size
        }

}