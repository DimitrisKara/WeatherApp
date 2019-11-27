package com.example.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WeatherDB.db";
    public static final String TABLE_NAME = "weather_table";
    public static final String COL_1 = "Date";
    public static final String COL_2 = "Temperature";
    public static final String COL_3 = "Weather";
    public static final String COL_4 = "City";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (Date TEXT,Temperature INTEGER,Weather TEXT,City TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Date, String Temperature, String Weather, String City) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Date);
        contentValues.put(COL_2, Temperature);
        contentValues.put(COL_3, Weather);
        contentValues.put(COL_4, City);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String Date, String Temperature, String Weather, String City) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,Date);
        contentValues.put(COL_2,Temperature);
        contentValues.put(COL_3,Weather);
        contentValues.put(COL_4,City);
        db.update(TABLE_NAME, contentValues, "City = ?",new String[] { City });
        return true;
    }

    public Integer deleteData (String City) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "City = ?",new String[] {City});
    }
}
