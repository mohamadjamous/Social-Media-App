package com.example.socialmediaapp.viewmodels

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialmediaapp.models.Post
import com.example.socialmediaapp.models.User
import com.example.socialmediaapp.utils.FeedState
import com.example.socialmediaapp.utils.LoadState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class FeedViewModel : ViewModel() {


    private val auth = FirebaseAuth.getInstance()
    private val _feedState = MutableLiveData<FeedState>()
    val feedState: LiveData<FeedState> = _feedState
    val postsLiveData: MutableState<List<Post>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    private val _loadState = MutableLiveData<LoadState>(LoadState.LOADING) // Start with loading state
    val loadState: LiveData<LoadState> get() = _loadState

    val emptyPost = Post(
        "",
        "",
        "https://firebasestorage.googleapis.com/v0/b/social-media-app-9c892.appspot.com/o/profile_images%2FtzR3HS87KKc8aq9zFnpd8f3f5ZM2.jpg?alt=media&token=c950a3aa-8341-4adb-97a5-7ee229882ed0",
        "User Name",
        "1730389310032",
        true,
        "https://firebasestorage.googleapis.com/v0/b/social-media-app-9c892.appspot.com/o/post_images%2FtzR3HS87KKc8aq9zFnpd8f3f5ZM2.jpg?alt=media&token=9fa73dba-456c-448c-bfe9-b207325372b0",
        "Caption Caption Caption Caption Caption Caption Caption Caption Caption Caption",
        10,
        listOf(),
        listOf(),
        isLiked = false,
        isFollowing = false
    )
    val post: MutableState<Post> = mutableStateOf(emptyPost)

    val scope = MainScope() // the scope of MyUIClass, uses Dispatchers.Main


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


    fun fetchPost(postId: String) {
        val db = FirebaseFirestore.getInstance()
        println("Started fetching post")

        _loadState.postValue(LoadState.LOADING)

        db.collection("posts").document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    post.value = document.toObject(Post::class.java) ?: emptyPost
                    println("Fetched post successfully: ${post.value}")
                    _loadState.postValue(LoadState.SUCCESS)
                } else {
                    println("Post does not exist")
                    post.value = emptyPost
                    _loadState.postValue(LoadState.ERROR)
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching post: ${e.message}")
                post.value = emptyPost
                _loadState.postValue(LoadState.ERROR)
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


    suspend fun likePost(postId: String): Int? {
        val db = FirebaseFirestore.getInstance()
        val postRef = db.collection("posts").document(postId)

        return try {
            // Update the "likes" array by adding the user's uid
            postRef.update("likes", FieldValue.arrayUnion(auth.currentUser!!.uid)).await()

            // Fetch the updated document to get the new likes count
            val document = postRef.get().await()
            val likes = document.get("likes") as? List<*>
            val likesCount = likes?.size ?: 0

            println("Updated likes count: $likesCount")
            likesCount
        } catch (e: Exception) {
            println("Error updating or fetching likes count: ${e.message}")
            null
        }
    }


    suspend fun disLikePost(postId: String): Int? {
        val db = FirebaseFirestore.getInstance()
        val postRef = db.collection("posts").document(postId)

        return try {
            // Update the "likes" array by removing the user's uid
            postRef.update("likes", FieldValue.arrayRemove(auth.currentUser!!.uid)).await()

            // Fetch the updated document to get the new likes count
            val document = postRef.get().await()
            val likes = document.get("likes") as? List<*>
            val likesCount = likes?.size ?: 0

            println("Updated likes count: $likesCount")
            likesCount
        } catch (e: Exception) {
            println("Error updating or fetching likes count: ${e.message}")
            null
        }
    }


    // Get all posts for timeline feed page
    fun fetchPosts() {

        val db = FirebaseFirestore.getInstance()
        _feedState.value = FeedState.Loading
        isLoading.value = true

        db.collection("posts").get()
            .addOnSuccessListener { documents ->
                val tempPosts = mutableListOf<Post>()

                for (document in documents) {
                    if (document != null && document.exists()) {

                        val post = document.toObject<Post>()
                        println("FollowerSize: ${post.followers.size}")
                        println("LikesSize: ${post.likes.size}")
                        post.postId = document.id
                        post.isLiked = post.likes.contains(auth.currentUser?.uid)
                        post.isFollowing = post.followers.contains(auth.currentUser?.uid)
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

    suspend fun updateFollowState(postId: String, followState: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val postRef = db.collection("posts").document(postId)

        return try {

            if (followState) {
                // Update the "likes" array by removing the user's uid
                postRef.update("followers", FieldValue.arrayUnion(auth.currentUser!!.uid)).await()
            } else {
                // Update the "likes" array by removing the user's uid
                postRef.update("followers", FieldValue.arrayRemove(auth.currentUser!!.uid)).await()
            }

            println("Updated follower count")
        } catch (e: Exception) {
            println("Error updating or fetching follower count: ${e.message}")
        }
    }

}



