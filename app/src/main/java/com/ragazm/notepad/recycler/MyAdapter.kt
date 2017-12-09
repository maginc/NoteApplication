package com.ragazm.notepad.recycler


import android.content.Context

import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.ragazm.notepad.Note
import com.ragazm.notepad.R

import java.util.ArrayList

/**
 * Created by Andris on 023 23.10.17.
 *
 * Initialize MyViewHolder class
 * Bind views to data
 * Pack data to be send to new activity
 * Open new activity and pass data to it
 *
 */

class MyAdapter(internal var context: Context, private var notes: ArrayList<Note>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        //Bind row_layout.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        // Initialize view holder

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.txtTitle.text = notes[position].title
        holder.txtText.text = notes[position].text

    }

    override fun getItemCount(): Int {

        return notes.size

    }
}
