package com.example.socialmediaapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.R
import com.example.socialmediaapp.view.ui.theme.SocialMediaAppTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaAppTheme {
                LoginScreen{

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onClick: () -> Unit) {

   var emailAddress by remember { mutableStateOf("")}
   var password by remember { mutableStateOf("")}

    Column(modifier = Modifier.padding(all = 10.dp)) {

        BackButton(
            modifier = Modifier
                .padding(top=15.dp)
                .padding(15.dp)
        ) {
            onClick()
        }

        Text("Hello Again!",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(15.dp)
                .padding(top = 20.dp)
                .fillMaxWidth()

        )

        Text("Sign in to your account",
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
                .padding(start = 40.dp)
                .padding(end = 40.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.green),  // Border color when focused
                unfocusedBorderColor = colorResource(id = R.color.grey)  // Border color when not focused
            ),
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(start = 40.dp)
                .padding(end = 40.dp)
        )

        Text("forgot your password?",
            color = colorResource(id = R.color.green)
        , modifier = Modifier
                .padding(top = 30.dp)
                .padding(start = 10.dp)
        )



        GreenButton(title = "Sign in", modifier = Modifier.padding(top = 70.dp)) {

        }


        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorResource(id = R.color.grey))) {
                    append("Don't have account? ")
                }
                withStyle(style = SpanStyle(color = colorResource(id = R.color.green))) {
                    append("Let's Sign up")
                }
            },
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth()
        )



    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    SocialMediaAppTheme {
        LoginScreen{

        }
    }
}