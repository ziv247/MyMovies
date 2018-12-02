package com.example1.ziv24.mymovies1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ziv24 on 28/03/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String cmd = "CREATE TABLE " + Constants.TABLE_NAME + " (_id INTEGER PRIMARY KEY, " +
                    Constants.COLUMN_NAME_original_title + " TEXT, " +
                    Constants.COLUMN_NAME_overview + " TEXT, " +
                    Constants.COLUMN_NAME_vote_average + " REAL, " +
                    Constants.COLUMN_NAME_poster + " TEXT);";

            db.execSQL(cmd);
        } catch (SQLiteException e) {
            e.getCause();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
