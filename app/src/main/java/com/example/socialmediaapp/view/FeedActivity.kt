package com.example.socialmediaapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialmediaapp.view.ui.theme.SocialMediaAppTheme

class FeedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaAppTheme {
                FeedScreen()
            }
        }
    }
}

@Composable
fun FeedScreen() {
    Text(
        text = "Hello"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    SocialMediaAppTheme {
        FeedScreen()
    }
}