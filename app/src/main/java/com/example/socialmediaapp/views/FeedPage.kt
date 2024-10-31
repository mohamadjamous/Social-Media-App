package com.example.socialmediaapp.views

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmediaapp.R
import com.example.socialmediaapp.viewmodels.FeedState
import com.example.socialmediaapp.viewmodels.FeedViewModel
import com.example.socialmediaapp.views.components.CircularProgress
import com.example.socialmediaapp.views.components.GreenButton
import com.example.socialmediaapp.views.ui.theme.SocialMediaAppTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    feedViewModel: FeedViewModel
) {


    val context = LocalContext.current
    var posts = feedViewModel.postsLiveData
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    val feedState = feedViewModel.feedState.observeAsState()
    var isLoading = false


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it // Update the selected image URI
            showBottomSheet = true
        }
    }

    LaunchedEffect(feedState.value) {

        when (feedState.value) {

            is FeedState.Published -> {
                showBottomSheet = false
                Toast.makeText(context, "Published Successfully", Toast.LENGTH_SHORT).show()
                feedViewModel.fetchPosts()
            }
            is FeedState.Error -> {
                Toast.makeText(
                    context,
                    (feedState.value as FeedState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                isLoading = false
            }

            is FeedState.Loading -> {
                isLoading = true
            }

            is FeedState.DoneProcessing ->
            {
                isLoading = false
            }

            else -> Unit
        }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (isLoading){
            CircularProgress()
        }


        Row(
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(start = 10.dp)
                .padding(end = 10.dp)
        ) {


            Column {
                Surface(onClick = {
                    launcher.launch("image/*")
                }) {
                    Image(
                        painterResource(id = R.drawable.camera),
                        contentDescription = "image",
                        modifier = Modifier.size(30.dp)

                    )
                }

                Text(text = "Timeline", fontSize = 20.sp, modifier = Modifier.padding(top = 20.dp))

                Surface(onClick = {
                    navController.navigate("profile")
                }) {
                    Text(
                        text = "Profile",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(250.dp))


            Image(painterResource(id = R.drawable.search),
                contentDescription = "image",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navController.navigate("search") }
            )

        }


        LazyColumn {
           items(posts.value.size){
               PostView(post = posts.value[it])
           }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                // Sheet content

                var caption by remember { mutableStateOf("") }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }) {
                            Image(
                                painterResource(id = R.drawable.baseline_cancel_24),
                                contentDescription = "image",
                                modifier = Modifier.size(30.dp)

                            )
                        }
                    }

                    Image(
                        painter = rememberAsyncImagePainter(model = selectedImageUri),
                        contentDescription = "image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(10.dp)

                    )


                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("Caption") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorResource(id = R.color.green),  // Border color when focused
                            unfocusedBorderColor = colorResource(id = R.color.grey)  // Border color when not focused
                        ),
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .padding(start = 20.dp)
                            .padding(end = 20.dp)
                    )

                    GreenButton(
                        title = "Publish Post",
                        modifier = Modifier
                            .padding(top = 100.dp)
                            .padding(start = 60.dp)
                            .padding(end = 60.dp)
                            .animateContentSize(),
                        enabled = true,
                        isLoading = feedState.value == FeedState.Loading
                    ) {
                        // Upload post to firebase
                        feedViewModel.uploadPost(selectedImageUri!!, caption)
                    }
                }
            }


        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun FeedPreview() {
    SocialMediaAppTheme {
        val navController = rememberNavController()
        FeedPage(
            modifier = Modifier,
            navController = navController,
            feedViewModel = FeedViewModel()
        )
    }
}