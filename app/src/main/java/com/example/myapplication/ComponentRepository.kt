package com.example.myapplication.data.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class ComponentRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val database: SQLiteDatabase = dbHelper.writableDatabase

    fun insertComponent(component: Component) {
        val contentValues = ContentValues().apply {
            put(KEY_COMPONENT_NAME, component.name)
            put(KEY_DANGER_LEVEL, component.dangerLevel)
            put(KEY_E_NUMBER, component.eNumber)
        }
        database.insert(TABLE_COMPONENT, null, contentValues)
    }



    companion object {
        private const val KEY_COMPONENT_ID = "id"
        private const val KEY_COMPONENT_NAME = "name"
        private const val KEY_DANGER_LEVEL = "danger_level"
        private const val KEY_E_NUMBER = "e_number"
        private const val TABLE_COMPONENT = "components"
    }
}
