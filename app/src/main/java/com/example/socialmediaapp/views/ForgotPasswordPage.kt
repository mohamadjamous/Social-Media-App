package com.example.socialmediaapp.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.viewmodels.AuthViewModel


@Composable
fun ForgotPasswordPage(modifier: Modifier = Modifier,
                       navController: NavController,
                       authViewModel: AuthViewModel) {


    Text("Forgot Password Page")
}



@Preview
@Composable
fun ForgotPasswordPagePreview() {


    ForgotPasswordPage(navController = rememberNavController(), authViewModel = AuthViewModel())
}