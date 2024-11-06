package com.example.socialmediaapp.models

import java.io.Serializable

data class Post(
    val uid: String, var postId: String, val profileImage: String, val userName: String, val milliseconds: String,
    var followState: Boolean, val postImage: String, val caption: String, val comments: Int,
    val likes: List<String>
) : Serializable {

    constructor() : this("", "",
        "", "", "", false, "",
        "", 0, listOf()
    )

}