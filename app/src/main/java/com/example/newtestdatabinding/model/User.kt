package com.example.newtestdatabinding.model

class User {

    data class User(
        val id : String = "",
        val name: String = "",
        val email: String = "",
        val phone: String = ""
    )
}