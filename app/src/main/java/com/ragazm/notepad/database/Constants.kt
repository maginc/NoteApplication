package com.ragazm.notepad.database

/**
 * Created by Andris on 023 23.10.17.
 * Define constants for database
 */

object Constants {

    //Columns
    val ROW_ID = "id"
    val TITLE = "title"
    val TEXT = "text"

    // Database properties
    val DB_NAME = "b_DB"
    val TB_NAME = "b_TB"
    val DB_VERSION = 1

    // Create table
    val CREATE_TB = "CREATE TABLE b_TB(id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT NOT NULL,text TEXT NOT NULL);"
}
