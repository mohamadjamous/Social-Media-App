package com.example.socialmediaapp.views

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewmodels.AuthState
import com.example.socialmediaapp.viewmodels.AuthViewModel
import com.example.socialmediaapp.views.components.BackButton
import com.example.socialmediaapp.views.components.GreenButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var emailAddress by remember { mutableStateOf("") }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> Toast.makeText(
                context, "Reset email has been sent!", Toast.LENGTH_SHORT
            ).show()
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }


    Column {
        BackButton(
            modifier = Modifier
                .padding(top = 15.dp)
                .padding(15.dp)
        ) {
            navController.popBackStack()
        }



        Text("Reset password",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 15.dp)
                .padding(top = 25.dp)
        )

        Text("Enter your email address to reset password",
            fontSize = 15.sp,
            modifier = Modifier
                .padding(start = 15.dp)
                .padding(top = 15.dp)
        )

        OutlinedTextField(
            value = emailAddress,
            onValueChange = { emailAddress = it },
            label = { Text("Email address") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.green),  // Border color when focused
                unfocusedBorderColor = colorResource(id = R.color.grey)  // Border color when not focused
            ),
            modifier = Modifier
                .padding(top = 70.dp)
                .padding(start = 25.dp)
                .padding(end = 25.dp)
                .fillMaxWidth()
        )


        GreenButton(
            title = "Reset Password",
            modifier = Modifier
                .padding(25.dp)
                .padding(top = 30.dp)
                .animateContentSize(),
            enabled = true,
            isLoading = authState.value == AuthState.Loading
        ) {
            authViewModel.resetPassword(emailAddress)
        }

    }

}


@Preview(showSystemUi = true)
@Composable
fun ForgotPasswordPagePreview() {


    ForgotPasswordPage(navController = rememberNavController(), authViewModel = AuthViewModel())
}