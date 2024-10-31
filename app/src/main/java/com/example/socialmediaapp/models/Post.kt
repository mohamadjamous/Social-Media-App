package com.example.socialmediaapp.models

data class Post(
    val profileImage: String, val userName: String, val milliseconds: String,
    val followState: Boolean, val postImage: String, val caption: String,
    val likes: Int, val comments: Int
) {

    constructor() : this(
        "", "", "", false, "",
        "", 0, 0
    )

}