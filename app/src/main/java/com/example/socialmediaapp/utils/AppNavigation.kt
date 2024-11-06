package com.example.socialmediaapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.models.Post
import com.example.socialmediaapp.viewmodels.AuthViewModel
import com.example.socialmediaapp.viewmodels.FeedViewModel
import com.example.socialmediaapp.views.FeedPage
import com.example.socialmediaapp.views.ForgotPasswordPage
import com.example.socialmediaapp.views.IntroPage
import com.example.socialmediaapp.views.LoginPage
import com.example.socialmediaapp.views.PostDetailsPage
import com.example.socialmediaapp.views.ProfilePage
import com.example.socialmediaapp.views.SignupPage


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    startDestination: String
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination, builder = {

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
            FeedPage(navController = navController, feedViewModel = FeedViewModel())
        }

        composable("forgotpassword") {
            ForgotPasswordPage(navController = navController, authViewModel = authViewModel)
        }

        composable("search") {

        }

        composable("profile") {
            ProfilePage(navController = navController, authViewModel = authViewModel)
        }

        composable("post_details/{postId}") { backStackEntry ->
            // Retrieve the postId from the backStackEntry arguments
            val postId = backStackEntry.arguments?.getString("postId")

            // Pass the postId to the PostDetailsPage Composable
            PostDetailsPage(
                navController = navController,
                postId = postId,
                feedViewModel = FeedViewModel()
            )
        }


    })

}