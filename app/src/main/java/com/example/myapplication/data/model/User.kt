package com.example.myapplication.data.model

data class User(
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String,
    val isDoctor: Boolean
)