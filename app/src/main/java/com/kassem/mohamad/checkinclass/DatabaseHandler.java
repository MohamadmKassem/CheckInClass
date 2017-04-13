package com.kassem.mohamad.checkinclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamad on 4/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "";

    // Class table name
    private static final String CLASS_TABLE = "";

    // Class table columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL_PROF = "emailprof";
    private static final String KEY_LOCATION = "location";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_class_table = "crate table" + CLASS_TABLE + " ( " + KEY_ID + " integer primary key, " + KEY_NAME + " text, " + KEY_EMAIL_PROF +" text, " + KEY_LOCATION + " text )";
        sqLiteDatabase.execSQL(create_class_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    void addClass(Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(KEY_NAME, c.getName());
        values.put(KEY_EMAIL_PROF, c.getEmail());
        values.put(KEY_LOCATION, c.getLocation());

        // Inserting row
        db.insert(CLASS_TABLE, null, values);
        db.close();
    }

    Class getClass(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CLASS_TABLE, new String[] {KEY_ID, KEY_NAME, KEY_EMAIL_PROF, KEY_LOCATION}, KEY_ID +"=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            Class c = new Class(cursor.getString(1), cursor.getString(0), cursor.getString(2),cursor.getString(3));
            db.close();
            return c;
        }
        return null;
    }
}
