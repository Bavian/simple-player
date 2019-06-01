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

    override fun compareTo(other: CompositionsList): Int {

        if (size != other.size) {
            return size - other.size
        }

        for (i in 0 until size) {

            if (compositions[i] != other.get(i)) {
                return compositions[i].compareTo(other.get(i))
            }

        }

        return 0

    }

}