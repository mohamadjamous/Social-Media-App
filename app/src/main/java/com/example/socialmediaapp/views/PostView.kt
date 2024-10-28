package com.example.socialmediaapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.socialmediaapp.R
import com.example.socialmediaapp.models.Post
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme

@Composable
fun PostView(post: Post) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row(modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {


        Image(
            painter = rememberAsyncImagePainter(model = post.imagePath),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)

        )

            Column(modifier = Modifier.padding(start = 10.dp)) {

                Text(text = post.userName,
                    fontSize = 20.sp)

                Text(text = post.timePosted,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 5.dp))
            }

            Spacer(modifier = Modifier.width(70.dp))


            if (post.followState){
                Text("Following", modifier = Modifier.padding(top = 20.dp))
            }else{
                Text("Follow", modifier = Modifier.padding(top = 20.dp))
            }



        }

        Image(
            painter = rememberAsyncImagePainter(model = post.postImagePath),
            contentDescription = "post image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()

        )

        Text(text = post.caption,
            color = Color.Gray,
            modifier = Modifier.padding(top = 15.dp))

        Row(modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()){

            Image(painter = painterResource(id = R.drawable.share),
                contentDescription = "")

            Spacer(modifier = Modifier.width(190.dp))

            Image(painter = painterResource(id = R.drawable.like),
                contentDescription = "")

            Text(text = post.comments.toString(),
                modifier = Modifier.padding(start = 5.dp))

            Image(painter = painterResource(id = R.drawable.baseline_comment_24),
                contentDescription = "",
                modifier = Modifier.padding(start = 25.dp))

            Text(text = post.comments.toString(),
                modifier = Modifier.padding(start = 5.dp))
    }

    }

}




@Preview(showSystemUi = true)
@Composable
fun PostPreview(){
    SocialMediaAppTheme {
        PostView(post = Post("https://img.freepik.com/premium-photo/wide-angle-shot-single-tree-growing-clouded-sky-sunset-surrounded-by-grass_1033124-10.jpg",
            "User Name",
            "52 mintues ago",
            true,
            "https://img.freepik.com/premium-photo/wide-angle-shot-single-tree-growing-clouded-sky-sunset-surrounded-by-grass_1033124-10.jpg",
            "Caption Caption Caption Caption Caption Caption Caption Caption Caption Caption",
            10,20))
    }
}