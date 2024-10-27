package com.example.socialmediaapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginViewModel: ViewModel() {


    var emailAddress by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set


    fun signInUser(email: String, password: String){

        var auth = Firebase.auth

//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    println("SignInSuccessful")
//                    val user = auth.currentUser
////                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    println("signInWithEmail:failure"+ task.exception)
////                    updateUI(null)
//                }
//            }


    }





}