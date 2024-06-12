package com.example.myapplication.data.model

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import com.example.myapplication.data.Disease
import com.example.myapplication.data.DiseaseData

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_PRODUCT_TABLE)
        db.execSQL(CREATE_DISEASE_TABLE)
        db.execSQL(CREATE_COMPONENT_TABLE)
        insertInitialData(db) // Вставляем начальные данные
        insertInitialProducts(db) // Вставляем начальные продукты
        insertInitialComponents(db) // Вставляем начальные компоненты
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DISEASE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMPONENT")
        onCreate(db)
    }

    @SuppressLint("Range")
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

    @SuppressLint("Range")
    fun getDiseaseDataByName(name: String): DiseaseData? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_DISEASE,
            arrayOf(KEY_DISEASE_NAME, KEY_DISEASE_DESCRIPTION, KEY_DISEASE_RISK_FACTORS, KEY_DISEASE_SYMPTOMS, KEY_DISEASE_DIAGNOSTICS, KEY_DISEASE_TREATMENTS),
            "$KEY_DISEASE_NAME = ?",
            arrayOf(name),
            null, null, null
        )

        return if (cursor != null && cursor.moveToFirst()) {
            val disease = DiseaseData(
                name = cursor.getString(cursor.getColumnIndex(KEY_DISEASE_NAME)),
                description = cursor.getString(cursor.getColumnIndex(KEY_DISEASE_DESCRIPTION)),
                causes = cursor.getString(cursor.getColumnIndex(KEY_DISEASE_RISK_FACTORS)),
                symptoms = cursor.getString(cursor.getColumnIndex(KEY_DISEASE_SYMPTOMS)),
                diagnostics = cursor.getString(cursor.getColumnIndex(KEY_DISEASE_DIAGNOSTICS)),
                treatment = cursor.getString(cursor.getColumnIndex(KEY_DISEASE_TREATMENTS))
            )
            cursor.close()
            disease
        } else {
            cursor.close()
            null
        }
    }

    @SuppressLint("Range")
    fun getUser(username: String, password: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(KEY_USERNAME, KEY_PASSWORD, KEY_NAME, KEY_SURNAME, KEY_PHONE, KEY_IS_DOCTOR),
            "$KEY_USERNAME = ? AND $KEY_PASSWORD = ?",
            arrayOf(username, password),
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
            cursor.close()
            null
        }
    }


    fun insertUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USERNAME, user.username)
            put(KEY_PASSWORD, user.password)
            put(KEY_NAME, user.name)
            put(KEY_SURNAME, user.surname)
            put(KEY_PHONE, user.phone)
            put(KEY_IS_DOCTOR, if (user.isDoctor) 1 else 0)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }

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

    @SuppressLint("Range")
    fun getComponentsByDiet(diet: String): List<Component> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_COMPONENT,
            arrayOf(KEY_COMPONENT_NAME, KEY_COMPONENT_E_NUMBER, KEY_COMPONENT_DANGER_LEVEL, KEY_COMPONENT_DIET),
            "$KEY_COMPONENT_DIET = ?",
            arrayOf(diet),
            null, null, null
        )
        val components = mutableListOf<Component>()
        if (cursor.moveToFirst()) {
            do {
                val component = Component(
                    cursor.getString(cursor.getColumnIndex(KEY_COMPONENT_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_COMPONENT_E_NUMBER)),
                    cursor.getInt(cursor.getColumnIndex(KEY_COMPONENT_DANGER_LEVEL)),
                    cursor.getString(cursor.getColumnIndex(KEY_COMPONENT_DIET))
                )
                components.add(component)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return components
    }



    @SuppressLint("Range")
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
            val values = ContentValues().apply {
                put(KEY_DISEASE_NAME, disease.name)
                put(KEY_DISEASE_DESCRIPTION, disease.description)
                put(KEY_DISEASE_RISK_FACTORS, disease.causes)
                put(KEY_DISEASE_SYMPTOMS, disease.symptoms)
                put(KEY_DISEASE_DIAGNOSTICS, disease.diagnostics)
                put(KEY_DISEASE_TREATMENTS, disease.treatment)
            }
            db.insert(TABLE_DISEASE, null, values)
        }
    }

    private fun insertInitialComponents(db: SQLiteDatabase) {
        val components = listOf(
            Component(name = "Куркумин", eNumber = "E100i", dangerLevel = 3, diet = "любая"),
            Component(name = "Рибофлавин", eNumber = "E101", dangerLevel = 3, diet = "любая"),
            Component(name = "Тартразин", eNumber = "E102", dangerLevel = 2, diet = "любая"),
            Component(name = "Алканит/алканет", eNumber = "E103", dangerLevel = 2, diet = "любая"),
            Component(name = "Желтый хинолиновый", eNumber = "E104", dangerLevel = 3, diet = "любая"),
            Component(name = "Эритрозин", eNumber = "E127", dangerLevel = 1, diet = "любая"),
            Component(name = "Желтый 2G", eNumber = "E107", dangerLevel = 2, diet = "любая"),
            Component(name = "Кармины", eNumber = "E120", dangerLevel = 3, diet = "любая"),
            Component(name = "Цитрусовый красный 2", eNumber = "E121", dangerLevel = 1, diet = "любая"),
            Component(name = "Амарант", eNumber = "E123", dangerLevel = 1, diet = "любая")
        )

        for (component in components) {
            val values = ContentValues().apply {
                put(KEY_COMPONENT_NAME, component.name)
                put(KEY_COMPONENT_E_NUMBER, component.eNumber)
                put(KEY_COMPONENT_DANGER_LEVEL, component.dangerLevel)
                put(KEY_COMPONENT_DIET, component.diet)
            }
            db.insert(TABLE_COMPONENT, null, values)
        }
    }


    private fun insertInitialProducts(db: SQLiteDatabase) {
        val products = listOf(
            Product(name = "Авокадо", usefulness = -2, diet = "любая"),
            Product(name = "Айран", usefulness = 0, diet = "любая"),
            Product(name = "Ананас", usefulness = -6, diet = "любая"),
            Product(name = "Апельсин", usefulness = -4, diet = "любая"),
            Product(name = "Арахис", usefulness = -7, diet = "любая"),
            Product(name = "Арбуз", usefulness = -2, diet = "любая"),
            Product(name = "Артишок", usefulness = -3, diet = "любая"),
            Product(name = "Банан", usefulness = -6, diet = "любая"),
            Product(name = "Брокколи", usefulness = 10, diet = "любая"),
            Product(name = "Яблоко", usefulness = 8, diet = "любая"),
            Product(name = "Гречка", usefulness = 7, diet = "безглютеновая"),
            Product(name = "Капуста", usefulness = 9, diet = "вегетарианская"),
            Product(name = "Морковь", usefulness = 6, diet = "любая"),
            Product(name = "Овсянка", usefulness = 8, diet = "вегетарианская"),
            Product(name = "Киви", usefulness = 5, diet = "любая"),
            Product(name = "Клубника", usefulness = 7, diet = "любая"),
            Product(name = "Чечевица", usefulness = 9, diet = "вегетарианская"),
            Product(name = "Миндаль", usefulness = 10, diet = "веганская"),
            Product(name = "Лосось", usefulness = 9, diet = "кето"),
            Product(name = "Курица", usefulness = 8, diet = "низкоуглеводная")
        )

        for (product in products) {
            val values = ContentValues().apply {
                put(KEY_PRODUCT_NAME, product.name)
                put(KEY_USEFULNESS, product.usefulness)
                put(KEY_DIET, product.diet)
            }
            db.insert(TABLE_PRODUCT, null, values)
        }
    }



    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "app_db"
        private const val TABLE_USER = "user"
        private const val TABLE_PRODUCT = "product"
        private const val TABLE_DISEASE = "disease"
        private const val TABLE_COMPONENT = "components"

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


        private const val KEY_COMPONENT_ID = "id"
        private const val KEY_COMPONENT_NAME = "name"
        private const val KEY_COMPONENT_E_NUMBER = "e_number"
        private const val KEY_COMPONENT_DANGER_LEVEL = "danger_level"
        private const val KEY_COMPONENT_DIET = "diet"

        private const val CREATE_COMPONENT_TABLE = (
                "CREATE TABLE " + TABLE_COMPONENT + "(" +
                        KEY_COMPONENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        KEY_COMPONENT_NAME + " TEXT NOT NULL," +
                        KEY_COMPONENT_E_NUMBER + " TEXT NOT NULL," +
                        KEY_COMPONENT_DANGER_LEVEL + " INTEGER NOT NULL," +
                        KEY_COMPONENT_DIET + " TEXT NOT NULL" + ")"
                )


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

        // Создание таблицы болезней
        private const val CREATE_DISEASE_TABLE = (
                "CREATE TABLE " + TABLE_DISEASE + "(" +
                        KEY_DISEASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        KEY_DISEASE_NAME + " TEXT," +
                        KEY_DISEASE_DESCRIPTION + " TEXT," +
                        KEY_DISEASE_RISK_FACTORS + " TEXT," +
                        KEY_DISEASE_SYMPTOMS + " TEXT," +
                        KEY_DISEASE_DIAGNOSTICS + " TEXT," +
                        KEY_DISEASE_TREATMENTS + " TEXT" + ")"
                )
    }
}
