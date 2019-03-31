package com.example.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "BMI.db";
    public static final String TABLE_NAME = "bmi_table";
    public static final String COL_2 = "BMI";
    public static final String COL_3 = "TIMESTAMP";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // super(context, name, factor, version)}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,BMI INTERGER, TIMESTAMP TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a record to the database
    public boolean insertData(String bmiData, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, bmiData);
        contentValues.put(COL_3, timestamp);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    // Method to show all the records
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

// Method to update a record

    public boolean updateData(String bmiData, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, bmiData);
        contentValues.put(COL_3, timestamp);
        db.update(TABLE_NAME, contentValues, "COL_3 = ?", new String[]{timestamp});
        return true;
    }

    // Method to delete a record
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

}
