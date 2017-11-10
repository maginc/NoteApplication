package com.ragazm.noteapplicationtest;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ragazm.noteapplicationtest.database.DBAdapter;

/**
 * Created by Andris on 026 26.10.17.
 */

public class Dialog {



    public Dialog() {

    }


    public void alertDialog(final Context activityContext, final int id){
        AlertDialog.Builder builder;
        // Context context;
        builder = new AlertDialog.Builder(activityContext);

        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBAdapter database = new DBAdapter(activityContext);
                        database.openDB();
                        database.delete(id);
                        database.close();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing if pressed NO
                    }
                })

                .show();

    }

}

