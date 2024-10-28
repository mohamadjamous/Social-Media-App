package com.example.socialmediaapp.views

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.viewmodels.AuthState
import com.example.socialmediaapp.viewmodels.AuthViewModel
import com.example.socialmediaapp.views.ui.theme.SocialMediaAppTheme


@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    TextButton(onClick = {
        authViewModel.signout()
    }) {
        Text(text = "Sign out")
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
    SocialMediaAppTheme {
        val navController = rememberNavController()

        ProfilePage(
            modifier = Modifier,
            navController = navController,
            authViewModel = AuthViewModel()
        )
    }
}
