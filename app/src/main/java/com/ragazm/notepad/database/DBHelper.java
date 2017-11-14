package com.ragazm.notepad.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andris on 023 23.10.17.
 * Database helper class for creating tables and updating them to new version
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Constants.CREATE_TB);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TB_NAME);
        onCreate(db);

    }
}
