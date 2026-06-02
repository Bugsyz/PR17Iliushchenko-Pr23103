package com.example.pr17_23101_fi

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, "MyDB", null, 1) {

    companion object {
        private const val LOG_TAG = "myLogs"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(LOG_TAG, "--- onCreate database ---")
        db?.execSQL(
            "CREATE TABLE mytable (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "email TEXT);"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}