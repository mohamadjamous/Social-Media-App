package com.example.socialmediaapp.views

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import com.example.socialmediaapp.views.ui.theme.SocialMediaAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var emailAddress by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("feed")
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }


    Column(modifier = Modifier.padding(all = 10.dp)) {

        BackButton(
            modifier = Modifier
                .padding(top = 15.dp)
                .padding(15.dp)
        ) {
            navController.popBackStack()
        }

        Text(
            "Hello Again!",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(15.dp)
                .padding(top = 20.dp)
                .fillMaxWidth()

        )

        Text(
            "Sign in to your account",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.grey),
            modifier = Modifier
                .fillMaxWidth()

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
                .padding(top = 40.dp)
                .padding(start = 45.dp)
                .padding(end = 40.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            placeholder = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Lock
                else Icons.Outlined.Lock

                // Description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.green),  // Focused border color
                unfocusedBorderColor = colorResource(id = R.color.grey)  // Unfocused border color
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(start = 45.dp, end = 40.dp)  // Combined padding
        )

        ClickableText(
            text = AnnotatedString("forgot your password?"),
            onClick = { navController.navigate("forgotpassword") },
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(start = 10.dp)
        )


        GreenButton(
            title = "Sign in", modifier = Modifier.padding(top = 70.dp),
            enabled = true,
            isLoading = authState.value is AuthState.Loading,
        ) {
            authViewModel.login(emailAddress, password)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            contentAlignment = Alignment.Center  // Align the text in the center
        ) {
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorResource(id = R.color.grey))) {
                    append("Don't have account? ")
                }
                pushStringAnnotation(
                    tag = "SIGN_UP",  // Tag for clickable text
                    annotation = "SignUp"
                )
                withStyle(style = SpanStyle(color = colorResource(id = R.color.green))) {
                    append("Let's Sign up")
                }
                pop()  // End of clickable span
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = "SIGN_UP",
                        start = offset,
                        end = offset
                    )
                        .firstOrNull()?.let {
                            // Handle click on "Let's Sign up"
                            navController.navigate("signup")
                        }
                },
                style = TextStyle(
                    fontSize = 18.sp,              // Set the font size
                    textAlign = TextAlign.Center   // Center the text
                ),
                modifier = Modifier.fillMaxWidth() // Ensures the text takes full width for alignment
            )
        }


    }

}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    SocialMediaAppTheme {
        val navController = rememberNavController()

        LoginPage(
            modifier = Modifier,
            navController = navController,
            authViewModel = AuthViewModel()
        )
    }
}
