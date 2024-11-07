package com.example.socialmediaapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.R
import com.example.socialmediaapp.models.Post
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import com.example.socialmediaapp.viewmodels.FeedViewModel
import com.example.socialmediaapp.views.components.LikeButton
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.Locale

@Composable
fun PostView(
    post: Post,
    feedViewModel: FeedViewModel = FeedViewModel(),
    onCommentClick: () -> Unit
) {

    var isFollowing by remember { mutableStateOf(post.isFollowing) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Set likesCount only once when the composable loads
    var likesCount by remember { mutableIntStateOf(post.likes.size) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {


            GlideImage(
                imageModel = post.profileImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)

            )

            Column(modifier = Modifier.padding(start = 10.dp)) {

                Text(
                    text = post.userName,
                    fontSize = 20.sp
                )

                Text(
                    text = formatTimeAgo(post.milliseconds.toLong()),
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }

            Spacer(modifier = Modifier.width(100.dp))


            if (isFollowing) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            feedViewModel.updateFollowState(post.postId, false)
                            isFollowing = false
                        }
                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text("Following")
                }
            } else {
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            coroutineScope.launch {
                                feedViewModel.updateFollowState(post.postId, true)
                                isFollowing = true
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text("Follow")
                }
            }


        }


        GlideImage(
            imageModel = post.postImage,
            contentDescription = "null",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .padding(top = 5.dp)

        )

        Text(
            text = post.caption,
            color = Color.Gray,
            modifier = Modifier.padding(top = 15.dp)
        )

        Row(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.share),
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(235.dp))

            LikeButton(like = post.isLiked,
                onLike = {
                    coroutineScope.launch {
                        likesCount = feedViewModel.likePost(post.postId)!!
                    }
                }, onDislike = {
                    coroutineScope.launch {
                        likesCount = feedViewModel.disLikePost(post.postId)!!
                    }
                })

            Text(
                text = likesCount.toString(),
                modifier = Modifier.padding(start = 5.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_comment_24),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 25.dp)
                    .clickable(onClick = {
                        onCommentClick()
                    })
            )

            Text(
                text = post.comments.toString(),
                modifier = Modifier.padding(start = 5.dp)
            )
        }

    }

}

fun formatTimeAgo(milliseconds: Long): String {
    val prettyTime = PrettyTime(Locale.getDefault())
    return prettyTime.format(Date(milliseconds))
}


@Preview(showSystemUi = true)
@Composable
fun PostPreview() {
    SocialMediaAppTheme {
        PostView(
            post = Post(
                "",
                "",
                "https://firebasestorage.googleapis.com/v0/b/social-media-app-9c892.appspot.com/o/profile_images%2FtzR3HS87KKc8aq9zFnpd8f3f5ZM2.jpg?alt=media&token=c950a3aa-8341-4adb-97a5-7ee229882ed0",
                "User Name",
                "1730389310032",
                true,
                "https://firebasestorage.googleapis.com/v0/b/social-media-app-9c892.appspot.com/o/post_images%2FtzR3HS87KKc8aq9zFnpd8f3f5ZM2.jpg?alt=media&token=9fa73dba-456c-448c-bfe9-b207325372b0",
                "Caption Caption Caption Caption Caption Caption Caption Caption Caption Caption",
                10,
                listOf(),
                listOf(),
                isLiked = false,
                isFollowing = false
            ), feedViewModel = FeedViewModel(),
            onCommentClick = {}
        )
    }
}
