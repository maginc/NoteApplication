package com.ragazm.notepad.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent


/**
 * Created by Andris on 025 25.10.17.
 */

class RecyclerTouchListener(context: Context, recyclerView: RecyclerView,
                            private val itemClickListener: ItemClickListener?) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null && itemClickListener != null) {
                    itemClickListener.onItemPress(childView,
                            recyclerView.getChildAdapterPosition(childView))
                }
            }
        })


    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

        val childView = rv.findChildViewUnder(e.x, e.y)
        if (childView != null && itemClickListener != null && gestureDetector.onTouchEvent(e)) {
            itemClickListener.onItemClick(childView, rv.getChildAdapterPosition(childView))
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}
