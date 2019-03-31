package com.example.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TODO.db";
    public static final String TABLE_NAME = "todo_table";
    public static final String COL_2 = "TASKNAME";
    public static final String COL_3 = "CLOCK";
    public static final String COL_4 = "FINISHED";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); // super(context, name, factor, version)}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASKNAME TEXT,CLOCK INTERGER, FINISHED INTERGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a record to the database
    public boolean insertData(String taskName, int clock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, taskName);
        contentValues.put(COL_3, clock);
        contentValues.put(COL_4, 0);

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
    public boolean updateData(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, taskName);
        contentValues.put(COL_3, COL_3+1);
        db.update(TABLE_NAME, contentValues, "COL_2 = ?", new String[]{taskName});
        return true;
    }

    // Method to delete a record
    public Integer deleteData(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "COL_2 = ?", new String[]{taskName});
    }

}
