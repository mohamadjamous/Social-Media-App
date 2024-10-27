package com.example.socialmediaapp.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewmodels.AuthState
import com.example.socialmediaapp.viewmodels.AuthViewModel
import com.example.socialmediaapp.viewmodels.SignupViewModel
import com.example.socialmediaapp.views.components.BackButton
import com.example.socialmediaapp.views.components.GreenButton
import com.example.socialmediaapp.views.ui.theme.SocialMediaAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) {

    var fullName by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf(createUserName(fullName = fullName)) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current  // Get the context from the composable
    val authState = authViewModel.authState.observeAsState()


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


    Box(contentAlignment = Alignment.Center) {

        Column(modifier = Modifier.padding(all = 10.dp)) {
            BackButton(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .padding(15.dp)
            ) {
                navController.popBackStack()
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Text(
                    "Create Account!",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(15.dp)
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                )

                ProfilePhotoPicker(selectedImageUri = selectedImageUri,
                    onImageSelected = { uri ->
                        selectedImageUri = uri  // Update the selected image URI
                    })

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        userName = createUserName(fullName)
                    },
                    label = { Text("Full Name") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.green),  // Border color when focused
                        unfocusedBorderColor = colorResource(id = R.color.grey)  // Border color when not focused
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(start = 25.dp)
                        .padding(end = 10.dp)
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
                        .padding(top = 10.dp)
                        .padding(start = 25.dp)
                        .padding(end = 10.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Username") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.green),  // Border color when focused
                        unfocusedBorderColor = colorResource(id = R.color.grey)  // Border color when not focused
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(start = 25.dp)
                        .padding(end = 10.dp)
                        .fillMaxWidth()
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
                        .padding(top = 10.dp)
                        .padding(start = 25.dp)
                        .padding(end = 10.dp)
                        .fillMaxWidth()
                )


                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    placeholder = { Text("Confirm Password") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            Icons.Filled.Lock
                        else Icons.Outlined.Lock

                        // Description for accessibility services
                        val description =
                            if (confirmPasswordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.green),  // Focused border color
                        unfocusedBorderColor = colorResource(id = R.color.grey)  // Unfocused border color
                    ),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(start = 25.dp)
                        .padding(end = 10.dp)
                        .fillMaxWidth()
                )


                GreenButton(
                    title = "Sign up",
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .animateContentSize(),
                    enabled = true,
                    isLoading = authState.value == AuthState.Loading
                ) {

                    // check if user info are valid
                    if (!isValidInfo(
                            selectedImageUri = selectedImageUri,
                            fullName = fullName,
                            email = emailAddress,
                            password = password,
                            userName = userName
                        )
                    ) {
                        Toast.makeText(context, "Info missing!", Toast.LENGTH_SHORT).show()
                    } else if (!checkPassword(password = password)) {
                        Toast.makeText(context, "Password is weak!", Toast.LENGTH_SHORT).show()
                    } else if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords don't match!", Toast.LENGTH_SHORT).show()
                    } else {
                        authViewModel.signup(
                            fullName = fullName,
                            email = emailAddress,
                            password = password,
                            userName = userName,
                            imageUri = selectedImageUri
                        )

                    }

                }
            }

        }
    }

}

@Composable
fun ProfilePhotoPicker(
    selectedImageUri: Uri?,  // Accept the URI from the parent composable
    onImageSelected: (Uri) -> Unit  // Callback to return the selected image URI
) {

    // Launcher for picking an image from the gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onImageSelected(it)  // Return the selected image URI to the parent composable
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    ) {
        // Show selected image or a placeholder if none is selected
        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedImageUri),
                contentDescription = "Selected Profile Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder image (e.g., default avatar)
            Image(
                painter = painterResource(id = R.drawable.person), // Replace with your placeholder image
                contentDescription = "Default Profile Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Add a plus button overlay for selecting an image
        IconButton(
            onClick = {
                // Launch photo picker when button is clicked
                launcher.launch("image/*")
            },
            modifier = Modifier
                .padding(bottom = 10.dp)
                .padding(end = 10.dp)
                .clip(CircleShape)
                .background(colorResource(id = R.color.grey))
                .align(Alignment.BottomEnd)

        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Photo",
                tint = Color.White,
            )
        }
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


@Preview(showSystemUi = true)
@Composable
fun SignupPreview() {
    SocialMediaAppTheme {
        val navController = rememberNavController()

        SignupPage(navController = navController, authViewModel = AuthViewModel())
    }
}
