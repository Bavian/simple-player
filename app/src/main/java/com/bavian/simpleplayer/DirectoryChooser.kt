package com.bavian.simpleplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.io.File

const val EXTERNAL_STORAGE = 1
val ROOT: File = Environment.getExternalStorageDirectory()

class DirectoryChooser : AppCompatActivity() {

    private var listView : ListView? = null
    private var currentFile : File? = null
    private var currentFiles : ArrayList<File>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_directory)
    }

    override fun onStart() {
        super.onStart()

        listView = this.findViewById(R.id.directories)

        val lv = listView

        lv!!.onItemClickListener = AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
            browseTo(currentFiles!![i])
        }

        if (ContextCompat.checkSelfPermission(
                this@DirectoryChooser,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this@DirectoryChooser,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE
            )

        } else {
            browseTo(ROOT)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            EXTERNAL_STORAGE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    browseTo(ROOT)
                } else {
                    onBackPressed()
                }

                return

            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun browseTo(fileToBrowse: File) {

        this.currentFile = fileToBrowse

        val files = fileToBrowse.listFiles()
        val directories = ArrayList<String>()
        currentFiles = ArrayList()

        if (fileToBrowse.parent != null && fileToBrowse != ROOT) {
            directories.add("..")
            currentFiles!!.add(fileToBrowse.parentFile)
        }

        for ( file in files ) {

            if (file.isDirectory && !file.isHidden) {
                directories.add(file.absolutePath.substring(file.absolutePath.lastIndexOf('/') + 1))
                currentFiles!!.add(file)
            }

        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directories)
        listView!!.adapter = adapter

    }

    fun done(view: View) {
        intent = Intent(this, PlayerInterface::class.java)

        val files = currentFile!!.listFiles { file -> getFileExtension(file) == ".mp3"}

        if (files.isEmpty()) {
            Toast.makeText(applicationContext, "В данной директории нет .mp3 файлов", Toast.LENGTH_SHORT).show()
        } else {

            var i = 0
            val filePaths = Array<String>(files.size) {files[i++].absolutePath}

            intent.putExtra("paths", filePaths)
            startActivity(intent)
        }

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

    override fun onBackPressed() {

        if (currentFile == ROOT) {
            super.onBackPressed()
        } else {
            browseTo(currentFiles!![0])
        }

    }

}
