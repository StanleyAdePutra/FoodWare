package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginManager {
    private static final String DATABASE_NAME = "user_login.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IS_LOGGED_IN = "is_logged_in";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0);";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public LoginManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertUser(String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);

        database.insert(TABLE_NAME, null, contentValues);
    }

    public void saveLoginStatus(String email, boolean isLoggedIn) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        database.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public boolean isLoggedIn() {
        String[] columns = {
                COLUMN_IS_LOGGED_IN
        };

        String selection = COLUMN_IS_LOGGED_IN + " = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = database.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();

        return isLoggedIn;
    }

    @SuppressLint("Range")
    public String getLoggedInUserEmail() {
        String[] columns = {
                COLUMN_EMAIL
        };

        String selection = COLUMN_IS_LOGGED_IN + " = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = database.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String userEmail = "";

        if (cursor.moveToFirst()) {
            userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
        }

        cursor.close();

        return userEmail;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void clearUserData() {
        database.delete(TABLE_NAME, null, null);
    }
}
