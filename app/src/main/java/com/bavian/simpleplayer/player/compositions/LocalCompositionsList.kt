package com.bavian.simpleplayer.player.compositions

import java.io.File

class LocalCompositionsList(path: String): CompositionsList {

    private val compositions = File(path).listFiles { file -> getFileExtension(file) == ".mp3" }

    override fun get(index: Int): String {
        return compositions[index].absolutePath
    }

    private fun getFileExtension(file: File): String {

        val name = file.name
        val lastIndexOf = name.lastIndexOf(".")

        return if (lastIndexOf == -1) {
            ""
        } else {
            name.substring(lastIndexOf)
        }

    }

}