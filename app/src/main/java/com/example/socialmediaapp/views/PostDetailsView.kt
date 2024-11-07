package com.example.socialmediaapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import com.example.socialmediaapp.utils.LoadState
import com.example.socialmediaapp.viewmodels.FeedViewModel
import com.example.socialmediaapp.views.components.BackButton
import com.example.socialmediaapp.views.components.CircularProgress
import com.example.socialmediaapp.views.components.LikeButton
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@Composable
fun PostDetailsPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    feedViewModel: FeedViewModel,
    postId: String
) {
    val post by feedViewModel.post
    val loadState by feedViewModel.loadState.observeAsState(LoadState.LOADING) // Default to LOADING
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(postId) {
        println("Triggering fetchPost with postId: $postId")
        feedViewModel.fetchPost(postId)
    }

    println("Current loadState: $loadState")
    println("Current post data: ${post.postId}")

    when (loadState) {
        LoadState.LOADING -> {
            CircularProgress()
        }

        LoadState.ERROR -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Error Fetching Post", color = Color.Red)
            }
        }

        LoadState.SUCCESS -> {
            if (post.postId.isNotEmpty()) {
                var likesCount by remember { mutableIntStateOf(post.likes.size) }

                Column {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            BackButton(modifier = Modifier.padding(15.dp)) {
                                navController.popBackStack()
                            }
                        }

                        GlideImage(
                            imageModel = post.postImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(400.dp)
                                .fillMaxWidth(),
                            loading = { CircularProgress() },
                            failure = { Text("Image failed to load") }
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(235.dp))

                            LikeButton(
                                like = post.isLiked,
                                onLike = {
                                    coroutineScope.launch {
                                        likesCount = feedViewModel.likePost(post.postId)!!
                                    }
                                },
                                onDislike = {
                                    coroutineScope.launch {
                                        likesCount = feedViewModel.disLikePost(post.postId)!!
                                    }
                                }
                            )

                            Text(
                                text = likesCount.toString(),
                                modifier = Modifier.padding(start = 5.dp)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.baseline_comment_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 25.dp)
                                    .clickable { /* Handle click */ }
                            )

                            Text(
                                text = post.comments.toString(),
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                }
            } else {
                Text("No post data available")
            }
        }
    }
}




@Composable
@Preview(showSystemUi = true)
fun PostDetailsPreview() {
    val navController = rememberNavController()

    SocialMediaAppTheme {
        PostDetailsPage(
            navController = navController, postId = "",
            feedViewModel = FeedViewModel()
        )
    }
}

