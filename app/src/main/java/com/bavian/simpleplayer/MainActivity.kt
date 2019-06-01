package com.bavian.simpleplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchGestureListener()
    }

    fun chooseDirectory(view: View) {
        val intent = Intent(this, DirectoryChooser::class.java)
        startActivity(intent)
    }

    private fun launchGestureListener() {
        val gdt = GestureDetector(this, GestureListener { moveTaskToBack(true) })

        val touchingView = findViewById<View>(R.id.main)
        touchingView.setOnTouchListener { _, event ->
            gdt.onTouchEvent(event)
            true
        }
    }

}
