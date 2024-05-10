package com.example.mydiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DB_NOTES = "diary.db";
    public static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DB_NOTES, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBContract.DBEntry.TABLE_TEXT
                + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBContract.DBEntry.COLUMN_NAME_DATE + " TEXT, " +
                DBContract.DBEntry.COLUMN_NAME_EMO + " TEXT, " +
                DBContract.DBEntry.COLUMN_NAME_DESCR + " TEXT, " +
                DBContract.DBEntry.COLUMN_NAME_IMG + " TEXT);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DBEntry.TABLE_TEXT);
        onCreate(db);
    }
}


