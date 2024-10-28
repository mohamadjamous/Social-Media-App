package com.example.socialmediaapp.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.views.ui.theme.SocialMediaAppTheme

@Composable
fun FeedPage(
    modifier: Modifier = Modifier,
    navController: NavController

) {

//    val authState = authViewModel.authState.observeAsState()
//
//    LaunchedEffect(authState.value) {
//        when(authState.value){
//            is AuthState.Unauthenticated -> navController.navigate("login")
//            else -> Unit
//        }
//    }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {

        }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier
            .padding(top = 40.dp)
            .padding(start = 10.dp)
            .padding(end = 10.dp)
        ) {



        Column{
            Surface(onClick = {
                launcher.launch("image/*")
                Toast.makeText(context, "Image clicked", Toast.LENGTH_SHORT).show()
            }) {
            Image(painterResource(id = R.drawable.camera),
                contentDescription = "image",
                modifier = Modifier.size(30.dp)

                    )
            }



            Text(text = "Timeline", fontSize = 20.sp, modifier = Modifier.padding(top = 20.dp))
        }

            Spacer(modifier = Modifier.width(250.dp))


            Image(painterResource(id = R.drawable.search),
                contentDescription = "image",
                modifier = Modifier.size(30.dp)
                    .clickable { navController.navigate("search") }
            )

        }

        TextButton(onClick = {
//            authViewModel.signout()
        }) {
            Text(text = "Sign out")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FeedPreview() {
    SocialMediaAppTheme {
        val navController= rememberNavController()
        FeedPage(
            modifier = Modifier,
            navController = navController)
    }
}