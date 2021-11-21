package com.bavian.simpleplayer.chooser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.io.File

class DirectoryChooserDirectoriesAdapter(private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<DirectoryChooserDirectoriesViewHolder>() {

    var fileNamesDataSet: List<String> = emptyList()

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): DirectoryChooserDirectoriesViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(android.R.layout.simple_list_item_1, viewGroup, false)
        return DirectoryChooserDirectoriesViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: DirectoryChooserDirectoriesViewHolder,
        position: Int
    ) {
        viewHolder.fileNameTextView.text = fileNamesDataSet[position]
        viewHolder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = fileNamesDataSet.size
}
