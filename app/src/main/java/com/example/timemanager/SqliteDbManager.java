package com.example.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SqliteDbManager {
    private static SqliteDbManager mInstance = null;
    private SQLiteDatabase mDb = null;
    private DatabaseHelper mDbHelper = null;
    String tbName = DatabaseHelper.TABLE_NAME;
    String col2 = DatabaseHelper.COL_2;
    String col3 = DatabaseHelper.COL_3;

    public static SqliteDbManager getInstance() {
        if (mInstance == null) {
            mInstance = new SqliteDbManager();
        }
        return mInstance;
    }

    public void setSqliteDbOpen(Context context) {
        mDbHelper = new DatabaseHelper(context.getApplicationContext());
        mDb = mDbHelper.getWritableDatabase();
    }

    public void insertTb(String bmi, String datestamp) {
        openDb();
        //方法一：
        Log.w("我进来了", "I am queryTb");
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, bmi);
        contentValues.put(col3, datestamp);
        mDb.insert(tbName, null, contentValues);
        closeDb();
    }


    public void updateTb(String tbName) {
        openDb();
        //方法一：

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", "隔壁老王");
        contentValues.put("sex", "男");
        mDb.update(tbName, contentValues, "name=?", new String[]{"王五"});
        //方法二：
        mDb.execSQL("update " + tbName + " set name = '哈利波特',age = '16' where name = '哈利'");
        closeDb();
    }

    public List<String> queryTb(String datestamp,String resultMes) {
        openDb();
        //方法一：
//        Log.w("我进来了","I am queryTb");
//        Cursor cursor = mDb.query(tbName, new String[]{"bmi","date"}, "date=?", new String[]{"隔壁老王"}, null, null, null);
//        //将光标移动到下一行，从而判断该结果集是否还有下一条数据；如果有则返回true，没有则返回false
//        if (null != cursor)
//        {
//            while (cursor.moveToNext())
//            {
//                String bmi = cursor.getString(cursor.getColumnIndex("BMI"));
//                int date = cursor.getInt(cursor.getColumnIndex("TIMESTAMP"));
//                Log.i("????","bmi = "+bmi+"; date = "+date);
//            }
//            cursor.close();
//        }
        //方法二：使用sql语句
        List<String> myList = new ArrayList<>();
        Cursor rawQuery = mDb.rawQuery("select * from " + tbName + " where TIMESTAMP=?", new String[]{datestamp});
        if (null != rawQuery) {
            while (rawQuery.moveToNext()) {
                String bmi = rawQuery.getString(rawQuery.getColumnIndex("BMI"));
                String date = rawQuery.getString(rawQuery.getColumnIndex("TIMESTAMP"));
                myList.add(resultMes + bmi + " (" + date + "）");
            }
            rawQuery.close();
        }
        closeDb();
        return myList;
    }

    public Collection getAll() {
        openDb();
        Collection allDate = new ArrayList<>();
        Cursor rawQuery = mDb.rawQuery("select * from " + tbName, null);
        if (null != rawQuery) {
            while (rawQuery.moveToNext()) {
                String date = rawQuery.getString(rawQuery.getColumnIndex("TIMESTAMP"));
                String subSentences[] = date.split("-");
            }
            rawQuery.close();
        }
        closeDb();
        return allDate;
    }

    /**
     * 创建或打开一个可以读的数据库
     */
    private void openDb() {
        if (this.mDbHelper != null) {
            try {
                mDb = mDbHelper.getWritableDatabase();
            } catch (Exception e) {
                mDb = mDbHelper.getReadableDatabase();
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据库
     */
    private void closeDb() {
        try {
            if (mDb != null) {
                mDb.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
