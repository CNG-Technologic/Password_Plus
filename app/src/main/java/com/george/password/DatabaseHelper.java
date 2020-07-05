package com.george.password;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "passwords.db"; // название бд
    private static final int VERSION = 1; // версия базы данных
    static final String TABLE = "users"; // название таблицы в бд

    // названия столбцов таблицы
    static final String ID = "_id";
    static final String WEB = "name";
    static final String PASSWORD = "code";
    static final String NAME_ACCOUNT = "nameAcc";

    DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WEB + " TEXT, " + PASSWORD + " TEXT, " + NAME_ACCOUNT + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

}