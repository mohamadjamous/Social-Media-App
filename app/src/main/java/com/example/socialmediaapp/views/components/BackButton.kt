package com.example.socialmediaapp.views.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.R

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier
            .width(70.dp)
            .height(50.dp),
        shape = RoundedCornerShape(12),
        colors = ButtonDefaults.textButtonColors(colorResource(id = R.color.green)),
        onClick = {
            onClick()
        }) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back icon",
            modifier = Modifier.size(24.dp),  // You can adjust the size as needed
            tint = Color.Unspecified          // Use this to retain the original color of the icon
        )
    }
}
