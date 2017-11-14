package com.ragazm.notepad.database;

/**
 * Created by Andris on 023 23.10.17.
 * Define constants for database
 */

public class Constants {

    //Columns
    static final String ROW_ID = "id";
    static final String TITLE = "title";
    static final String TEXT = "text";

    // Database properties
    static final String DB_NAME = "b_DB";
    static final String TB_NAME = "b_TB";
    static final int DB_VERSION = 1;

    // Create table
    static final String CREATE_TB="CREATE TABLE b_TB(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "title TEXT NOT NULL,text TEXT NOT NULL);";
}
