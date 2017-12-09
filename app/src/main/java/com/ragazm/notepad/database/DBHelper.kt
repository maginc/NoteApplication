package com.ragazm.notepad.database

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Andris on 023 23.10.17.
 * Database helper class for creating tables and updating them to new version
 */

class DBHelper(context: Context) : SQLiteOpenHelper(context, Constants.DB_NAME, null, Constants.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(Constants.CREATE_TB)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TB_NAME)
        onCreate(db)

    }
}
