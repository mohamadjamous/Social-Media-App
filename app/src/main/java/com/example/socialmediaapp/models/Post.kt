package com.example.socialmediaapp.models

data class Post(val imagePath: String, val userName: String, val timePosted: String,
    val followState: Boolean, val postImagePath: String, val caption: String,
                val likes: Int, val comments: Int)