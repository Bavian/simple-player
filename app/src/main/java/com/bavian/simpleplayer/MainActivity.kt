package com.bavian.simpleplayer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun chooseDirectory(view: View) {
        val intent = Intent(this, DirectoryChooser::class.java)
        startActivity(intent)
    }

}