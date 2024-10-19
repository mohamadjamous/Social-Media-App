package com.example.socialmediaapp.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class SignupViewModel : ViewModel() {

    // State representing UI states
    sealed class SignUpState {
        object Idle : SignUpState()
        object Loading : SignUpState()
        data class Success(val message: String) : SignUpState()
        data class Error(val errorMessage: String) : SignUpState()
    }

    var signUpState by mutableStateOf<SignUpState>(SignUpState.Idle)



    fun signupUser(
        fullName: String,
        email: String,
        password: String,
        userName: String,
        imageUri: Uri?
    ) {
        val auth = Firebase.auth
        val db = FirebaseFirestore.getInstance() // FireStore instance
        val storage = FirebaseStorage.getInstance() // Firebase Storage instance
        signUpState = SignUpState.Loading


        // Create user with email and password in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, create FireStore user document
                    val user = auth.currentUser
                    if (user != null) {
                        // If an image is selected, upload it to Firebase Storage
                        imageUri?.let { uri ->
                            val storageRef = storage.reference.child("profile_images/${user.uid}.jpg")
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
                                            signUpState = SignUpState.Success("User created successfully with profile image")
                                        }
                                        .addOnFailureListener { e ->
                                            println("Error saving user information: ${e.message}")
                                            signUpState = SignUpState.Error("Error saving user: ${e.message}")

                                        }
                                } else {
                                    println("Error getting download URL: ${task.exception?.message}")
                                    signUpState = SignUpState.Error("Error getting image download URL")

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
                                    signUpState = SignUpState.Success("User created successfully without profile image")

                                }
                                .addOnFailureListener { e ->
                                    println("Error saving user information: ${e.message}")
                                    signUpState = SignUpState.Error("Error saving user: ${e.message}")

                                }
                        }
                    }
                } else {
                    // If sign-up fails, display a message to the user.
                    println("createUserWithEmail:failure ${task.exception?.message}")
                    signUpState = SignUpState.Error("Sign up failed: ${task.exception?.message}")

                }
            }
    }


    fun createUserName(fullName: String): String {
        val names = fullName.trim().split(" ")
        return if (names.size >= 2) {
            // Combine the first letter of the first name with the full last name
            val firstNameInitial = names[0].take(1).lowercase()
            val lastName = names[1].lowercase()
            "$firstNameInitial$lastName"
        } else {
            // Return the full name lowercased if there's no space
            fullName.lowercase().replace(" ", "")
        }
    }

    fun checkPassword(password: String): Boolean {
        // Define basic password rules: at least 8 characters, contains upper, lower, digit, and special char
        val minLength = 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return password.length >= minLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
    }


    fun isValidInfo(
        selectedImageUri: Uri?,
        fullName: String,
        email: String,
        password: String,
        userName: String
    ): Boolean {

        println("UserInfo: $selectedImageUri")
        println("UserInfo: $fullName")
        println("UserInfo: $email")
        println("UserInfo: $password")
        println("UserInfo: $userName")


        // Check if image URI is valid (non-null and not empty)
        if (selectedImageUri == null) {
            return false
        }

        // Check if full name contains at least two words
        if (fullName.trim().split("\\s+".toRegex()).size < 2) {
            return false
        }

        // Check if email is valid using Android's Patterns
        if (email.isEmpty()) {
            return false
        }

        // Check if password meets minimum requirements (e.g., at least 8 characters)
        if (password.length < 8) {
            return false
        }

        // Check if username is not empty and meets length requirements (e.g., at least 3 characters)
        if (userName.isEmpty() || userName.length < 3) {
            return false
        }

        // If all validations pass, return true
        return true
    }

}