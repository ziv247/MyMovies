package com.example1.ziv24.mymovies1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example1.ziv24.mymovies1.Movies;

import java.util.ArrayList;

/**
 * Created by ziv24 on 28/03/2018.
 */


public class DBHandler {

    private DBHelper helper;

    public DBHandler(Context context) {
        helper = new DBHelper(context, Constants.DATABASE_NAME, null, 1);
    }

    public Boolean addMovie(String original_title, String overview, float vote_average, String poster) {

        SQLiteDatabase db = helper.getWritableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_NAME_original_title, original_title);
            values.put(Constants.COLUMN_NAME_overview, overview);
            values.put(Constants.COLUMN_NAME_vote_average, vote_average);
            values.put(Constants.COLUMN_NAME_poster, poster);
            int id = (int) db.insert(Constants.TABLE_NAME, null, values);
            Movies movie = new Movies(original_title, overview, vote_average, poster, id);
            return true;

        } catch (SQLiteException e) {
            return false;

        } finally {
            if (db.isOpen())
                db.close();

        }
    }

    public boolean deleteMovie(int id) {

        String pos = "" + id;
        SQLiteDatabase db = helper.getWritableDatabase();
        try {

            db.delete(Constants.TABLE_NAME, "_id=?", new String[]{pos});
        } catch (SQLiteException e) {
            return false;
        } finally {
            if (db.isOpen())
                db.close();
        }
        return true;
    }

    public boolean updateNote(Movies movies) {

        String title = movies.getOriginal_title();
        String overview = movies.getOverview();
        float votes = movies.getVote_average();
        String poster = movies.getPoster();


        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_NAME_original_title, title);
            values.put(Constants.COLUMN_NAME_overview, overview);
            values.put(Constants.COLUMN_NAME_vote_average, votes);
            values.put(Constants.COLUMN_NAME_poster, poster);

            db.update(Constants.TABLE_NAME, values, "_id=?", new String[]{String.valueOf(movies.getId())});
        } catch (SQLiteException e) {
            return false;
        } finally {
            if (db.isOpen())
                db.close();
        }
        return true;
    }

    public ArrayList<Movies> getAllNotes() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);

        } catch (SQLiteException e) {
            e.getCause();
        }

        ArrayList<Movies> table = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String original_title = cursor.getString(1);
            String overview = cursor.getString(2);
            float vote_average = cursor.getFloat(3);
            String poster = cursor.getString(4);


            table.add(new Movies(original_title, overview, vote_average, poster, id));
        }
        return table;
    }

    public Movies selectNote(String _id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {

            cursor = db.query(Constants.TABLE_NAME, null, "_id=?", new String[]{_id}, null, null, null);

        } catch (SQLiteException e) {
            e.getCause();
        }

        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String original_title = cursor.getString(1);
        String overview = cursor.getString(2);
        float vote_average = cursor.getFloat(3);
        String poster = cursor.getString(4);


        Movies table = new Movies(original_title, overview, vote_average, poster, id);

        return table;
    }

}
