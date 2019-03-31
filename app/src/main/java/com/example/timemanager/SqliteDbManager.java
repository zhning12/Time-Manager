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
    String col4 = DatabaseHelper.COL_4;

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

    public void insertTb(String taskName, int clock) {
        openDb();
        //方法一：
        Log.w("番茄钟 存入数据库", String.valueOf(clock));
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, taskName);
        contentValues.put(col3, clock);
        contentValues.put(col4, 0);
        mDb.insert(tbName, null, contentValues);
        closeDb();
    }

    public void updateTb(String taskName) {
        openDb();
        //方法一：

//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", "隔壁老王");
//        contentValues.put("sex", "男");
//        mDb.update(tbName, contentValues, "name=?", new String[]{"王五"});
        //方法二：
        mDb.execSQL("update " + tbName + " set " + col4 + " = " + col4 + "+ 1 where " + col2 + " = " + taskName);
        closeDb();
    }

    public List<String> queryTb(String datestamp, String resultMes) {
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

    public ArrayList<ToDoList> getAll() {
        openDb();
        Log.w("开始执行getall","执行啦啦啦啦啦啦");
        ArrayList<ToDoList> allTask = new ArrayList<>();
        Cursor rawQuery = mDb.rawQuery("select * from " + tbName, null);
        if (null != rawQuery) {
            while (rawQuery.moveToNext()) {
                ToDoList task = new ToDoList();
                task.taskName = rawQuery.getString(rawQuery.getColumnIndex(col2));
                task.clockNum = rawQuery.getInt(rawQuery.getColumnIndex(col3));
                task.finished = rawQuery.getInt(rawQuery.getColumnIndex(col4));
                allTask.add(task);
            }
            rawQuery.close();
        }
        closeDb();
        Log.w("开始打印第一个task",allTask.get(0).taskName);

        return allTask;
    }

    public Cursor getAllData() {
        openDb();
        Cursor res = mDb.rawQuery("select * from " + tbName, null);
        return res;
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
