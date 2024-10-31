package com.example.socialmediaapp.models

class User(val fullName: String, val email: String,
           var password: String, val userName: String, val profileImageUrl: String) {

    constructor() : this("", "", "", "", "")
}
