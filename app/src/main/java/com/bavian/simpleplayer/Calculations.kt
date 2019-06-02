package com.bavian.simpleplayer

fun getTimer(time: Int?): String {

    if (time == null) {
        return "0:00"
    }

    val seconds = time / 1000 % 60
    val minutes = time / 1000 / 60

    val secondsString = if (seconds < 10) "0$seconds" else seconds.toString()

    return "$minutes:$secondsString"
}