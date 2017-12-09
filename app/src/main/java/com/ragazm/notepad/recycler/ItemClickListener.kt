package com.ragazm.notepad.recycler

import android.view.View

/**
 * Created by Andris on 022 22.10.17.
 * Item click listener
 */


interface ItemClickListener {
    fun onItemClick(view: View, position: Int)
    fun onItemPress(view: View, position: Int)

}
