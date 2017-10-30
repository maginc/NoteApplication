package com.ragazm.noteapplicationtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Andris on 023 23.10.17.
 *
 * Initialize MyViewHolder class
 * Bind views to data
 * Pack data to be send to new activity
 * Open new activity and pass data to it
 *
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<Note> notes;

    public MyAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Bind row_layout.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        // Initialize view holder
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtTitle.setText(notes.get(position).getTitle());
        holder.txtText.setText(notes.get(position).getText());

    }

    @Override
    public int getItemCount() {

        return notes.size();

    }
}
