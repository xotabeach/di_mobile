package com.example.myapplication.data.model

data class User(
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val middlename: String,
    val birthdate: String,
    val phone: String,
    val image: String,
    val mail: String,
    val isDoctor: Boolean
) {

}
