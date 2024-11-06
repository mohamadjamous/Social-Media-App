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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.socialmediaapp.viewmodels.FeedViewModel
import com.example.socialmediaapp.viewmodels.LoadState
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
    postId: String?
) {
//    // Ensure post is being observed properly
//    val post = feedViewModel.post.value
//    val loadState = feedViewModel.loadState.value
//
//    // State variables for loading and error
//    var isLoading by remember { mutableStateOf(false) }
//    var isError by remember { mutableStateOf(false) }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    // Fetch post from firestore only once when postId is available
//    LaunchedEffect(postId) {
//        if (postId != null) {
//            feedViewModel.fetchPost(postId)
//        }
//    }
//
//    // Handle UI based on loadState
//    LaunchedEffect(loadState) {
//        when (loadState) {
//            LoadState.LOADING -> {
//                isLoading = true
//                isError = false
//            }
//
//            LoadState.SUCCESS -> {
//                isLoading = false
//                isError = false
//            }
//
//            LoadState.ERROR -> {
//                isLoading = false
//                isError = true
//            }
//
//            else -> {}
//        }
//    }
//
//    // Handle UI based on state
//    if (isLoading) {
//        CircularProgress()
//    } else if (isError) {
//        Box {
//            BackButton(modifier = Modifier.padding(15.dp)) {
//                navController.popBackStack()
//            }
//            Text("Error happened while loading")
//        }
//    } else {
//        // Safe access to post data
//        if (post != null) {
//            var likesCount by remember { mutableIntStateOf(post.likes.size) }
//            var isLiked by remember { mutableStateOf(false) }
//
//            // Check if the current user has liked the post only once when the composable loads
//            LaunchedEffect(post.postId) {
//                isLiked = feedViewModel.isUserLiked(post.postId)
//            }
//
//            Column {
//                Box {
//                    Row {
//                        BackButton(modifier = Modifier.padding(15.dp)) {
//                            navController.popBackStack()
//                        }
//                    }
//
//                    GlideImage(
//                        imageModel = post.postImage,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .height(400.dp)
//                            .fillMaxWidth()
//                    )
//
//                    Row(
//                        modifier = Modifier
//                            .padding(top = 30.dp)
//                            .fillMaxWidth()
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.share),
//                            contentDescription = null
//                        )
//
//                        Spacer(modifier = Modifier.width(190.dp))
//
//                        LikeButton(
//                            like = !isLiked,
//                            onLike = {
//                                coroutineScope.launch {
//                                    likesCount = feedViewModel.likePost(post.postId)!!
//                                }
//                            },
//                            onDislike = {
//                                coroutineScope.launch {
//                                    likesCount = feedViewModel.disLikePost(post.postId)!!
//                                }
//                            }
//                        )
//
//                        Text(
//                            text = likesCount.toString(),
//                            modifier = Modifier.padding(start = 5.dp)
//                        )
//
//                        Image(
//                            painter = painterResource(id = R.drawable.baseline_comment_24),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .padding(start = 25.dp)
//                                .clickable { /* Handle click */ }
//                        )
//
//                        Text(
//                            text = post.comments.toString(),
//                            modifier = Modifier.padding(start = 5.dp)
//                        )
//                    }
//                }
//            }
//        } else {
//            Text("No post found")
//        }
//    }
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

