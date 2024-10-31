package com.example.socialmediaapp.views

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.request.ImageRequest
import coil3.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.models.Post
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.Locale

@Composable
fun PostView(post: Post) {

    println("PostValue: " + post.postImage)
    println("PostValue: " + post.profileImage)
    println("PostValue: " + post.userName)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {


            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.postImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.person),
                error = painterResource(id = R.drawable.person),
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

            Spacer(modifier = Modifier.width(70.dp))


            if (post.followState) {
                Text("Following", modifier = Modifier.padding(top = 20.dp))
            } else {
                Text("Follow", modifier = Modifier.padding(top = 20.dp))
            }


        }

        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(post.postImage)
            .addHeader("Content-Type", post.profileImage) // or image/png if appropriate
            .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()

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

            Spacer(modifier = Modifier.width(190.dp))

            Image(
                painter = painterResource(id = R.drawable.like),
                contentDescription = ""
            )

            Text(
                text = post.comments.toString(),
                modifier = Modifier.padding(start = 5.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.baseline_comment_24),
                contentDescription = "",
                modifier = Modifier.padding(start = 25.dp)
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
                "https://firebasestorage.googleapis.com/v0/b/social-media-app-9c892.appspot.com/o/post_images%2FtzR3HS87KKc8aq9zFnpd8f3f5ZM2.jpg?alt=media&token=9fa73dba-456c-448c-bfe9-b207325372b0",
                "User Name",
                "1730389310032",
                true,
                "https://firebasestorage.googleapis.com/v0/b/social-media-app-9c892.appspot.com/o/post_images%2FtzR3HS87KKc8aq9zFnpd8f3f5ZM2.jpg?alt=media&token=9fa73dba-456c-448c-bfe9-b207325372b0",
                "Caption Caption Caption Caption Caption Caption Caption Caption Caption Caption",
                10, 20
            )
        )
    }
}