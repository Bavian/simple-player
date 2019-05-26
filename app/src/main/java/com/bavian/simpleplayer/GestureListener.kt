package com.bavian.simpleplayer

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity






private val SWIPE_MIN_DISTANCE = 120
private val SWIPE_THRESHOLD_VELOCITY = 200

class GestureListener(var action: () -> Unit) : SimpleOnGestureListener() {

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e2.y - e1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            action()
            return false // сверху вниз
        }
        return false
    }

}