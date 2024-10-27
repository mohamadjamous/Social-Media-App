package com.example.socialmediaapp.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmediaapp.R

@Composable
fun GreenButton(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12),
        colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.green)),
        onClick = {
            onClick()
        },
        enabled = enabled
    )
    {

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(35.dp)
            )
            Text("Loading...",
                fontSize = 16.sp,
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(start = 20.dp))
        }else{
            Text(
                text = title,
                fontSize = 16.sp,
                color = colorResource(id = R.color.white)
            )
        }



    }
}
