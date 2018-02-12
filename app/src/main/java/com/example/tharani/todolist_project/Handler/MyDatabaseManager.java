package com.example.tharani.todolist_project.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tharani on 1/6/2018.
 */

public class MyDatabaseManager  extends SQLiteOpenHelper {



    public class MyDatabaseHelper {

        public static final String DATABASE_NAME = "ToDo";
        public static final int DATABASE_VERSION = 1;
        public static final String COLUMN_ID = "key_id";
        public static final String COLUMN_TITLE = "key_title";
        public static final String COLUMN_DESCRIPTION = "key_description";
        public static final String COLUMN_DATE = "key_date";
        public static final String COLUMN_STATUS = "key_status";

    }

    Context context;

    public MyDatabaseManager(Context context) {
        super(context, MyDatabaseHelper.DATABASE_NAME, null, MyDatabaseHelper.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE " + MyDatabaseHelper.DATABASE_NAME + " ("
                + MyDatabaseHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MyDatabaseHelper.COLUMN_TITLE + " TEXT, " + MyDatabaseHelper.COLUMN_DATE + " TEXT, "
                + MyDatabaseHelper.COLUMN_DESCRIPTION + " TEXT, " + MyDatabaseHelper.COLUMN_STATUS + " INTEGER )";

        db.execSQL(table1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(MyDatabaseHelper.DATABASE_NAME);
        onCreate(db);

    }

    public void insert(String title, String description, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.COLUMN_TITLE, title);
        contentValues.put(MyDatabaseHelper.COLUMN_DESCRIPTION, description);
        contentValues.put(MyDatabaseHelper.COLUMN_DATE, date);
        contentValues.put(MyDatabaseHelper.COLUMN_STATUS, status);
        db.insert(MyDatabaseHelper.DATABASE_NAME, null, contentValues);
    }


    public Cursor fetch() {
        Log.d("data", "fetch");
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = new String[]{MyDatabaseHelper.COLUMN_ID, MyDatabaseHelper.COLUMN_TITLE, MyDatabaseHelper.COLUMN_DESCRIPTION, MyDatabaseHelper.COLUMN_DATE, MyDatabaseHelper.COLUMN_STATUS};
        Cursor cursor = db.query(MyDatabaseHelper.DATABASE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public int update(Integer _id, String title, String description, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.COLUMN_TITLE, title);
        contentValues.put(MyDatabaseHelper.COLUMN_DESCRIPTION, description);
        contentValues.put(MyDatabaseHelper.COLUMN_DATE, date);
        //contentValues.put(MyDatabaseHelper.COLUMN_STATUS, status);

        int i = db.update(MyDatabaseHelper.DATABASE_NAME, contentValues, MyDatabaseHelper.COLUMN_DESCRIPTION + " = " + _id, null);
        return i;
    }


    public void delete(long _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MyDatabaseHelper.DATABASE_NAME, MyDatabaseHelper.COLUMN_ID + " = " + _id, null);

    }

}

