package com.example.socialmediaapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.viewmodels.AuthViewModel
import com.example.socialmediaapp.views.FeedPage
import com.example.socialmediaapp.views.ForgotPasswordPage
import com.example.socialmediaapp.views.IntroPage
import com.example.socialmediaapp.views.LoginPage
import com.example.socialmediaapp.views.SignupPage


@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "intro", builder = {

        composable("intro") {
            IntroPage(navController = navController, authViewModel = authViewModel)
        }

        composable("login") {
            LoginPage(navController = navController, authViewModel = authViewModel)
        }

        composable("signup") {
            SignupPage(navController = navController, authViewModel = authViewModel)
        }

        composable("feed") {
//            FeedPage(navController = navController, authViewModel = authViewModel)
            FeedPage(navController = navController)
        }

        composable("forgotpassword") {
            ForgotPasswordPage(navController = navController, authViewModel = authViewModel)
        }

        composable("search") {

        }




    })

}