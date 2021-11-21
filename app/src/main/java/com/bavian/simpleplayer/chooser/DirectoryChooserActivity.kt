package com.bavian.simpleplayer.chooser

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.bavian.simpleplayer.PlayerInterface
import com.bavian.simpleplayer.R
import java.io.File

const val TAG = "DirectoryChooser"
const val EXTERNAL_STORAGE_REQUEST_CODE = 1

class DirectoryChooserActivity : AppCompatActivity() {

    private lateinit var directoriesRecyclerView: RecyclerView
    private val directoriesAdapter =
        DirectoryChooserDirectoriesAdapter { browseTo(directories[it]) }

    private var currentFile = startDirectory
    private val directories = ArrayList<File>()
    private val directoriesNames = ArrayList<String>()

    private val startDirectory: File
        get() = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory_chooser)
        directoriesRecyclerView =
            this.findViewById(R.id.directory_chooser_directories_recycler_view)
        directoriesRecyclerView.adapter = directoriesAdapter
        directoriesRecyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<ImageButton>(R.id.directory_chooser_chosen_button).setOnClickListener {
            onDirectoryChosen()
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_REQUEST_CODE
            )
        } else {
            browseTo(startDirectory)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE) {
            for (index in permissions.indices) {
                if (permissions[index] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                    if (grantResults.isNotEmpty() && grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        onBackPressed()
                        return
                    }
                    browseTo(startDirectory)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun browseTo(fileToBrowse: File) {
        Log.d(TAG, "browse to ${fileToBrowse.absolutePath}")
        currentFile = fileToBrowse
        val childDirectories = fileToBrowse.getDirectories()

        directories.clear()
        directories.ensureCapacity(childDirectories.size + 1)
        directoriesNames.clear()
        directoriesNames.ensureCapacity(childDirectories.size + 1)

        val parentFile = fileToBrowse.parentFile
        if (parentFile != null) {
            directories.add(parentFile)
            directoriesNames.add("..")
        }

        childDirectories.forEach {
            directories.add(it)
            directoriesNames.add(it.name)
        }
        directoriesAdapter.fileNamesDataSet = directoriesNames
        directoriesAdapter.notifyDataSetChanged()
    }

    private fun onDirectoryChosen() {
        val chosenDirectoryIntent = Intent(this, PlayerInterface::class.java)
        val tracks = currentFile.getTracks()

        if (tracks.isEmpty()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.directory_chooser_warning),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        chosenDirectoryIntent.putExtra("paths", tracks.map { it.absolutePath }.toTypedArray())
        startActivity(chosenDirectoryIntent)
    }

    private fun File.getDirectories() = listFiles { file: File -> file.isDirectory } ?: emptyArray()

    private fun File.getTracks(): Array<File> {
        val supportedFormats = resources.getStringArray(R.array.supported_formats)
        return listFiles { _, name: String ->
            supportedFormats.any { name.endsWith(it) }
        } ?: emptyArray()
    }
}
