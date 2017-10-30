package com.ragazm.noteapplicationtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Andris on 023 23.10.17.
 * Perform CRUD(CreateReadUpdateDelete) operations with database
 */

public class DBAdapter {
   Context context;
    SQLiteDatabase database;
    DBHelper helper;

    public DBAdapter(Context context) {
        this.context = context;
        helper = new DBHelper(context);
    }
    //      Open Database
    public DBAdapter openDB(){
        try{
            database = helper.getWritableDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return this;
    }

    //      Close Database
    public void close(){
        try{
            helper.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //     Insert data to Database
    public long add(String title, String text){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TITLE, title);
            contentValues.put(Constants.TEXT, text);
            return database.insert(Constants.TB_NAME, Constants.ROW_ID, contentValues);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    //     Retrieve all notes
    public Cursor getAllNotes(){
        String [] columns = {Constants.ROW_ID, Constants.TITLE, Constants.TEXT};
        return database.query(Constants.TB_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    //    Update
    public long update(int id, String title, String text){
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TITLE, title);
            contentValues.put(Constants.TEXT, text);
            return database.update(Constants.TB_NAME, contentValues, Constants.ROW_ID + " =?",
                    new String[]{String.valueOf(id)});
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    //   Delete
    public long delete(int id){
        try{
            return database.delete(Constants.TB_NAME, Constants.ROW_ID + " =?",
                    new String[]{String.valueOf(id)});
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }



}
