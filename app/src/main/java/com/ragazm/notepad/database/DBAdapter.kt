package com.ragazm.notepad.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

/**
 * Created by Andris on 023 23.10.17.
 * Perform CRUD(CreateReadUpdateDelete) operations with database
 */

class DBAdapter(internal var context: Context) {
    internal lateinit var database: SQLiteDatabase
    internal var helper: DBHelper

    //     Retrieve all notes
    val allNotes: Cursor
        get() {
            val columns = arrayOf(Constants.ROW_ID, Constants.TITLE, Constants.TEXT)
            return database.query(Constants.TB_NAME,
                    columns,
                    null, null, null, null, null, null)
        }

    init {
        helper = DBHelper(context)
    }

    //      Open Database
    fun openDB(): DBAdapter {
        try {
            database = helper.writableDatabase
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return this
    }

    //      Close Database
    fun close() {
        try {
            helper.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    //     Insert data to Database
    fun add(title: String, text: String): Long {
        try {
            val contentValues = ContentValues()
            contentValues.put(Constants.TITLE, title)
            contentValues.put(Constants.TEXT, text)
            return database.insert(Constants.TB_NAME, Constants.ROW_ID, contentValues)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return 0
    }

    //    Update
    fun update(id: Int, title: String, text: String): Long {
        try {
            val contentValues = ContentValues()
            contentValues.put(Constants.TITLE, title)
            contentValues.put(Constants.TEXT, text)
            return database.update(Constants.TB_NAME, contentValues, Constants.ROW_ID + " =?",
                    arrayOf(id.toString())).toLong()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return 0
    }

    //   Delete
    fun delete(id: Int): Long {
        try {
            return database.delete(Constants.TB_NAME, Constants.ROW_ID + " =?",
                    arrayOf(id.toString())).toLong()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return 0
    }


}
