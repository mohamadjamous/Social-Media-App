package com.example.socialmediaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignupViewModel : ViewModel() {


    fun signupUser(fullName: String, email: String, password: String, userName: String) {
        val auth = Firebase.auth
        val db = FirebaseFirestore.getInstance() // FireStore instance

        // Create user with email and password in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, create FireStore user document
                    val user = auth.currentUser
                    if (user != null) {
                        // Prepare the user data to save in Firestore
                        val userData = hashMapOf(
                            "fullName" to fullName,
                            "email" to email,
                            "userName" to userName,
                            "uid" to user.uid
                        )

                        // Save the user data in the "users" collection in Firestore
                        db.collection("users")
                            .document(user.uid) // The user's UID is used as the document ID
                            .set(userData)
                            .addOnSuccessListener {
                                println("User information saved successfully in Firestore!")
                                // Optionally, call updateUI(user) if needed
                            }
                            .addOnFailureListener { e ->
                                println("Error saving user information: ${e.message}")
                            }
                    }
                } else {
                    // If sign-up fails, display a message to the user.
                    println("createUserWithEmail:failure ${task.exception?.message}")
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


}