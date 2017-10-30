package com.ragazm.noteapplicationtest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

//import com.ragazm.noteapplicationtest.databinding.RowLayoutBinding;

/**
 * Created by Andris on 022 22.10.17.
 * TODO implement data binding
 */

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   // RowLayoutBinding rowLayoutBinding;


    TextView txtTitle;
    TextView txtText;
   

    public MyViewHolder(View itemView) {
        super(itemView);
        txtTitle = (TextView)itemView.findViewById(R.id.txtText);
        txtText = (TextView)itemView.findViewById(R.id.txtTitle);
        //itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
