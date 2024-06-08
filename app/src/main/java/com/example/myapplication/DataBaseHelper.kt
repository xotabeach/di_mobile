package com.example.myapplication.data.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    fun getUserByUsername(username: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(KEY_USERNAME, KEY_PASSWORD, KEY_NAME, KEY_SURNAME, KEY_PHONE, KEY_IS_DOCTOR),
            "$KEY_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val user = User(
                cursor.getString(cursor.getColumnIndex(KEY_USERNAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)),
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_SURNAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                cursor.getInt(cursor.getColumnIndex(KEY_IS_DOCTOR)) > 0
            )
            cursor.close()
            user
        } else {
            null
        }
    }

    fun insertUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_USERNAME, user.username)
        values.put(KEY_PASSWORD, user.password)
        values.put(KEY_NAME, user.name)
        values.put(KEY_SURNAME, user.surname)
        values.put(KEY_PHONE, user.phone)
        values.put(KEY_IS_DOCTOR, if (user.isDoctor) 1 else 0)
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "app_db"
        private const val TABLE_USER = "user"

        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NAME = "name"
        private const val KEY_SURNAME = "surname"
        private const val KEY_PHONE = "phone"
        private const val KEY_IS_DOCTOR = "is_doctor"

        private const val CREATE_USER_TABLE = (
                "CREATE TABLE " + TABLE_USER + "(" +
                        KEY_USERNAME + " TEXT PRIMARY KEY," +
                        KEY_PASSWORD + " TEXT," +
                        KEY_NAME + " TEXT," +
                        KEY_SURNAME + " TEXT," +
                        KEY_PHONE + " TEXT," +
                        KEY_IS_DOCTOR + " INTEGER" + ")"
                )
    }
}
