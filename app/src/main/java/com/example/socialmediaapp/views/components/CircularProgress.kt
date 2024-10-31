package com.example.socialmediaapp.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.R

@Composable
fun CircularProgress() = CircularProgressIndicator(
    Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
        .size(80.dp),
    color = colorResource(id = R.color.green)
)


@Composable
@Preview
fun CircularProgressPreview(){
    CircularProgress()
}