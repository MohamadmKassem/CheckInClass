package com.kassem.mohamad.checkinclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamad on 4/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dataBase";

    // Class table name
    private static final String CLASS_TABLE = "MyClass";

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
        String create_class_table = "create table MyClass ( " + KEY_ID + " text primary key, " + KEY_NAME + " text, " + KEY_EMAIL_PROF +" text, " + KEY_LOCATION + " text )";
        sqLiteDatabase.execSQL(create_class_table);
        create_class_table="create table Lectures ( id int primary key, date text, classId int, open text)";
        sqLiteDatabase.execSQL(create_class_table);
        create_class_table="create table Presence ( LectureId int , FullName text, Email text, here int)";
        sqLiteDatabase.execSQL(create_class_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    String updatePresence(int LectureId,ArrayList<presence>AP)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("Presence","LectureId="+LectureId,null);
        int i;
        String test="db";
        for(i=0;i<AP.size();i++)
        {
            ContentValues values  = new ContentValues();
            values.put("LectureId", LectureId);
            values.put("FullName", AP.get(i).fullname);
            values.put("Email", AP.get(i).email);
            int here=0;
            if(AP.get(i).here)
                here=1;
            values.put("here",here);
            db.insert("Presence", null, values);
            test+="/"+i;
        }
        db.close();
        return test;
    }

    void addClass(Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put(KEY_ID,c.getId());
        values.put(KEY_NAME, c.getName());
        values.put(KEY_EMAIL_PROF, c.getEmail());
        values.put(KEY_LOCATION, c.getLocation());

        // Inserting row
        db.insert(CLASS_TABLE, null, values);
        db.close();
    }
    void addLecture(int id,String date,int classid,String open)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values  = new ContentValues();
        values.put("id",id);
        values.put("date",date);
        values.put("classId",classid);
        values.put("open",open);
        db.insert("Lectures", null, values);
        db.close();
    }
    public void setlecture(String bool,int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues v=new ContentValues();
        v.put("open",bool);
        db.update("Lectures",v,"id="+id,null);
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
    public ArrayList<presence> getPresence(int LectureId)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="select * from Presence where LectureId="+LectureId;
        Cursor c=db.rawQuery(query,null);
        ArrayList<presence> con=new ArrayList<presence>();
        if(c.moveToFirst())
        {
            do
            {
                presence p=new presence(c.getString(1),c.getString(2),c.getInt(3)==1);
                con.add(p);
            }while(c.moveToNext());
        }
        db.close();
        return con;
    }
    public ArrayList<Class> getAllclass(String email)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="select * from MyClass where emailprof='"+email+"'";
        Cursor c=db.rawQuery(query,null);
        ArrayList<Class> con=new ArrayList<Class>();
        int nb=0;
        if(c.moveToFirst())
        {
            do
            {
                Class c1=new Class(c.getString(1),c.getString(0));
                con.add(c1);
            }while(c.moveToNext());
        }
        db.close();
        return con;
    }
    public void deleteLecture(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("Lectures","id="+id,null);
    }
    public void deleteClass(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("Lectures","classId="+id,null);
        db.delete("MyClass","id="+id,null);
    }
    public ArrayList<Lecture> getLectures(int classId)
    {
        String id=Integer.toString(classId);
        SQLiteDatabase db=this.getReadableDatabase();
        String query="select * from Lectures where classid='"+id+"'";
        Cursor c=db.rawQuery(query,null);
        int nb=1;
        ArrayList<Lecture> con=new ArrayList<Lecture>();
        if(c.moveToFirst())
        {
            do
            {
                boolean b=(c.getString(3).equals(new String("true")));
                Lecture l1=new Lecture(c.getString(1),b,nb,c.getInt(0),c.getInt(2));
                con.add(l1);
                nb++;

            }while(c.moveToNext());
        }
        db.close();
        return con;
    }

}
