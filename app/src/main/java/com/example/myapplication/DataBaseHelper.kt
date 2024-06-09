package com.example.myapplication.data.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.myapplication.data.Disease
import com.example.myapplication.data.DiseaseData

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_PRODUCT_TABLE)
        db.execSQL(CREATE_DISEASE_TABLE)// Добавляем создание таблицы продуктов
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DISEASE")// Добавляем удаление таблицы продуктов
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

    // Новый метод для вставки продуктов
    fun insertProduct(product: Product) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PRODUCT_NAME, product.name)
            put(KEY_USEFULNESS, product.usefulness)
            put(KEY_DIET, product.diet)
        }
        db.insert(TABLE_PRODUCT, null, values)
        db.close()
    }

    // Новый метод для получения продуктов по диете
    fun getProductsByDiet(diet: String): List<Product> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PRODUCT,
            arrayOf(KEY_ID, KEY_PRODUCT_NAME, KEY_USEFULNESS, KEY_DIET),
            "$KEY_DIET = ?",
            arrayOf(diet),
            null, null, null
        )
        val products = mutableListOf<Product>()
        if (cursor.moveToFirst()) {
            do {
                val product = Product(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME)),
                    cursor.getInt(cursor.getColumnIndex(KEY_USEFULNESS)),
                    cursor.getString(cursor.getColumnIndex(KEY_DIET))
                )
                products.add(product)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        for (disease in Disease.diseases) {
            val values = ContentValues()
            values.put(KEY_DISEASE_ID, disease.id)
            values.put(KEY_DISEASE_NAME, disease.name)
            values.put(KEY_DISEASE_DESCRIPTION, disease.description)
            values.put(KEY_DISEASE_RISK_FACTORS, disease.causes)
            values.put(KEY_DISEASE_SYMPTOMS, disease.symptoms)
            values.put(KEY_DISEASE_DIAGNOSTICS, disease.diagnostics)
            values.put(KEY_DISEASE_TREATMENTS, disease.treatment)
            db.insert(TABLE_DISEASE, null, values)
        }
    }


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "app_db"
        private const val TABLE_USER = "user"
        private const val TABLE_PRODUCT = "product"
        private const val TABLE_DISEASE = "disease"

        // Параметры для таблицы пользователей
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NAME = "name"
        private const val KEY_SURNAME = "surname"
        private const val KEY_PHONE = "phone"
        private const val KEY_IS_DOCTOR = "is_doctor"

        // Параметры для таблицы продуктов
        private const val KEY_ID = "id"
        private const val KEY_PRODUCT_NAME = "name"
        private const val KEY_USEFULNESS = "usefulness"
        private const val KEY_DIET = "diet"


        // Параметры для таблицы болезней
        private const val KEY_DISEASE_ID = "id"
        private const val KEY_DISEASE_NAME = "name"
        private const val KEY_DISEASE_DESCRIPTION = "description"
        private const val KEY_DISEASE_RISK_FACTORS = "causes"
        private const val KEY_DISEASE_SYMPTOMS = "symptoms"
        private const val KEY_DISEASE_DIAGNOSTICS = "diagnostics"
        private const val KEY_DISEASE_TREATMENTS = "treatment"


        // Создание таблицы пользователей
        private const val CREATE_USER_TABLE = (
                "CREATE TABLE " + TABLE_USER + "(" +
                        KEY_USERNAME + " TEXT PRIMARY KEY," +
                        KEY_PASSWORD + " TEXT," +
                        KEY_NAME + " TEXT," +
                        KEY_SURNAME + " TEXT," +
                        KEY_PHONE + " TEXT," +
                        KEY_IS_DOCTOR + " INTEGER" + ")"
                )

        // Создание таблицы продуктов
        private const val CREATE_PRODUCT_TABLE = (
                "CREATE TABLE " + TABLE_PRODUCT + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        KEY_PRODUCT_NAME + " TEXT NOT NULL," +
                        KEY_USEFULNESS + " INTEGER NOT NULL," +
                        KEY_DIET + " TEXT NOT NULL" + ")"
                )

        private const val CREATE_DISEASE_TABLE = (
                "CREATE TABLE " + TABLE_DISEASE + "(" +
                        KEY_DISEASE_ID + " INTEGER PRIMARY KEY," +
                        KEY_DISEASE_NAME + " TEXT," +
                        KEY_DISEASE_DESCRIPTION + " TEXT," +
                        KEY_DISEASE_RISK_FACTORS + " TEXT," +
                        KEY_DISEASE_SYMPTOMS + " TEXT," +
                        KEY_DISEASE_TREATMENTS + " TEXT" + ")"
                )


    }




}
