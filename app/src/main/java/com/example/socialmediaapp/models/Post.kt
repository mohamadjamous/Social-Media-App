package com.example.socialmediaapp.models

data class Post(
    val uid: String, val profileImage: String, val userName: String, val milliseconds: String,
    var followState: Boolean, val postImage: String, val caption: String, val comments: Int,
    val likes: List<String>
) {

    constructor() : this("",
        "", "", "", false, "",
        "", 0, listOf()
    )

}~