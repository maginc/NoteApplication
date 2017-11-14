package com.ragazm.notepad.recycler;

import android.view.View;

/**
 * Created by Andris on 022 22.10.17.
 * Item click listener
 */



public interface ItemClickListener {
    void onItemClick(View view, int position);
    void onItemPress(View view, int position);

}
