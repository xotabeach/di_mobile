package com.example.myapplication.data.model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    suspend fun getUserByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            dbHelper.getUserByUsername(username)
        }
    }

    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            dbHelper.insertUser(user)
        }
    }
}
