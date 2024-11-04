package com.example.socialmediaapp.viewmodels

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.models.Post
import com.example.socialmediaapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class FeedViewModel : ViewModel() {


    private val auth = FirebaseAuth.getInstance()
    private val _feedState = MutableLiveData<FeedState>()
    val feedState: LiveData<FeedState> = _feedState
    val postsLiveData: MutableState<List<Post>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    init {
        fetchPosts()
    }

    // Search for users


    // Publishing user post
    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadPost(imageUri: Uri, caption: String) {

        val db = FirebaseFirestore.getInstance() // FireStore instance
        val storage = FirebaseStorage.getInstance() // Firebase Storage instance
        _feedState.value = FeedState.Loading
        val user = auth.currentUser

        if (user != null) {

            val userAccount = fetchUser(user.uid)
            // If an image is selected, upload it to Firebase Storage
            imageUri.let { uri ->
                val storageRef =
                    storage.reference.child("post_images/${user.uid}.jpg")
                val uploadTask = storageRef.putFile(uri)

                // Monitor the upload and get the download URL upon success
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the image download URL
                        val downloadUri = task.result

                        // Prepare the user data to save in Firestore
                        val postData = hashMapOf(
                            "uid" to user.uid,
                            "profileImage" to userAccount.value!!.profileImageUrl,
                            "userName" to userAccount.value!!.userName,
                            "milliseconds" to getCurrentDateTimeInMilliseconds(),
                            "postImage" to downloadUri.toString(),
                            "caption" to caption
                        )

                        // Save the post data in the "posts" collection in Firestore
                        db.collection("posts")
                            .add(postData) // Adds a new document with a unique ID in the "posts" collection
                            .addOnSuccessListener {
                                println("User post information saved successfully in Firestore with image URL!")
                                // Optionally, call updateUI(user) if needed
                                _feedState.value = FeedState.Published
                            }
                            .addOnFailureListener { e ->
                                println("Error saving user information: ${e.message}")
                                _feedState.value = FeedState.Error(
                                    task.exception?.message ?: "Something went wrong"
                                )

                            }
                    } else {
                        println("Error getting download URL: ${task.exception?.message}")
                        _feedState.value = FeedState.Error(
                            task.exception?.message ?: "Something went wrong"
                        )

                    }
                }
            }
        }
    }

    // Get current user info
    private fun fetchUser(userId: String): MutableLiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject<User>() // Maps Firestore document to User class
                    userLiveData.value = user
                } else {
                    _feedState.value = FeedState.Error("Something went wrong")
                    userLiveData.value = null // Document doesn't exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure (e.g., log error, set userLiveData to null, etc.)
                _feedState.value = FeedState.Error(
                    exception.message ?: "Something went wrong"
                )
                userLiveData.value = null
            }

        return userLiveData
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTimeInMilliseconds(): String {
        val instant = Instant.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
        return instant.toEpochMilli().toString() // Returns milliseconds as String
    }


    fun likePost(postId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("posts")
            .document(postId).update("likes", FieldValue.arrayUnion(auth.currentUser!!.uid))
            .addOnSuccessListener {
                println("Post liked successfully!")
            }
            .addOnFailureListener { e ->
                println("Error liking post: ${e.message}")
            }
    }


    // Get all posts for timeline feed page
    fun fetchPosts(){

        val db = FirebaseFirestore.getInstance()
        _feedState.value = FeedState.Loading
        isLoading.value = true

        db.collection("posts").get()
            .addOnSuccessListener { documents ->
                val tempPosts = mutableListOf<Post>()

                for (document in documents) {
                    if (document != null && document.exists()) {
                        val post = document.toObject<Post>()
                        tempPosts.add(post)
                    }
                }

                if (tempPosts.isNotEmpty()) {
                    _feedState.value = FeedState.DoneProcessing
                    postsLiveData.value = tempPosts
                    println("PostsLiveDataSize: ${postsLiveData.value.size}")
                    println("PostsLiveDataSize: ${tempPosts.size}")
                    isLoading.value = false
                } else {
                    _feedState.value = FeedState.Error("Something went wrong")
                    postsLiveData.value = emptyList()
                    isLoading.value = false
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure (e.g., log error, set userLiveData to null, etc.)
                _feedState.value = FeedState.Error(exception.message ?: "Something went wrong")
                postsLiveData.value = emptyList()
                isLoading.value = false
            }

    }

}

sealed class FeedState {
    data object Loading : FeedState()
    data object Published : FeedState()
    data object DoneProcessing : FeedState()
    data class Error(val message: String) : FeedState()
}