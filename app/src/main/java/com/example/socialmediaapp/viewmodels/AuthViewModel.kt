package com.example.socialmediaapp.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }


    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signup(
        fullName: String,
        email: String,
        password: String,
        userName: String,
        imageUri: Uri?
    ) {
        val db = FirebaseFirestore.getInstance() // FireStore instance
        val storage = FirebaseStorage.getInstance() // Firebase Storage instance
        _authState.value = AuthState.Loading

        // Create user with email and password in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, create FireStore user document
                    val user = auth.currentUser
                    if (user != null) {
                        // If an image is selected, upload it to Firebase Storage
                        imageUri?.let { uri ->
                            val storageRef =
                                storage.reference.child("profile_images/${user.uid}.jpg")
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
                                    val userData = hashMapOf(
                                        "fullName" to fullName,
                                        "email" to email,
                                        "userName" to userName,
                                        "uid" to user.uid,
                                        "profileImageUrl" to downloadUri.toString() // Save image URL
                                    )

                                    // Save the user data in the "users" collection in Firestore
                                    db.collection("users")
                                        .document(user.uid) // The user's UID is used as the document ID
                                        .set(userData)
                                        .addOnSuccessListener {
                                            println("User information saved successfully in Firestore with profile image URL!")
                                            // Optionally, call updateUI(user) if needed
                                            _authState.value = AuthState.Authenticated
                                        }
                                        .addOnFailureListener { e ->
                                            println("Error saving user information: ${e.message}")
                                            _authState.value = AuthState.Error(
                                                task.exception?.message ?: "Something went wrong"
                                            )

                                        }
                                } else {
                                    println("Error getting download URL: ${task.exception?.message}")
                                    _authState.value = AuthState.Error(
                                        task.exception?.message ?: "Something went wrong"
                                    )

                                }
                            }
                        } ?: run {
                            // If no image is provided, save only the user info
                            val userData = hashMapOf(
                                "fullName" to fullName,
                                "email" to email,
                                "userName" to userName,
                                "uid" to user.uid
                            )

                            // Save user data without image URL
                            db.collection("users")
                                .document(user.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    println("User information saved successfully in FireStore without profile image!")
                                    _authState.value = AuthState.Authenticated

                                }
                                .addOnFailureListener { e ->
                                    println("Error saving user information: ${e.message}")
                                    _authState.value = AuthState.Error(
                                        task.exception?.message ?: "Something went wrong"
                                    )

                                }
                        }
                    }
                } else {
                    // If sign-up fails, display a message to the user.
                    println("createUserWithEmail:failure ${task.exception?.message}")
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")

                }
            }
    }


    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


    fun resetPassword(email: String) {

        _authState.value = AuthState.Loading
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: "Something went wrong"
                    )
                }
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(
                    it.message ?: "Something went wrong"
                )
            }
    }

}


sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}