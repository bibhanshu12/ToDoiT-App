package com.bibo.newrecycleview;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.PrivilegedExceptionAction;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=2;
    private  static  final String DATABASE_NAME="studentDB";
    private  static final String Table_Name="Student";

    private static final String col_1="ID";
    private static final String col_2="TASK";
    private  static  final String col_3="TASKNAME";


    private SQLiteDatabase sqLiteDatabase;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ Table_Name +" ( ID INTEGER PRIMARY KEY AUTOINCREMENT ,TASK TEXT  , TASKNAME TEXT)" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Table_Name);
        onCreate(db);
    }

    public boolean insertdata(String TASK,String TASKNAME) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2, TASK);
        contentValues.put(col_3, TASKNAME);

        long result=sqLiteDatabase.insert(Table_Name,null,contentValues);
        if(result==-1){
            return false;

        }else {
            return true;
        }


    }
    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery(" Select * from " + Table_Name,null);
        return c;



    }
    public boolean updateData(String ID,String TASKs,String TASKNAME){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(col_2,TASKs);
        contentValues.put(col_3,TASKNAME);

        sqLiteDatabase.update(Table_Name, contentValues, "ID = ?", new String[]{ID});
        return true;


    }

    public Integer deleteDataa(String ID){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();


        return sqLiteDatabase.delete(Table_Name,"ID = ?",new String[]{ID});


    }

}
