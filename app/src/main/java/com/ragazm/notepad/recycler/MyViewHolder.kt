package com.ragazm.notepad.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.ragazm.notepad.R



/**
 * Created by Andris on 022 22.10.17.
 *
 */

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    // RowLayoutBinding rowLayoutBinding;


    internal var txtTitle: TextView = itemView.findViewById(R.id.txtText) as TextView
    internal var txtText: TextView = itemView.findViewById(R.id.txtTitle) as TextView




    override fun onClick(v: View) {

    }
}
