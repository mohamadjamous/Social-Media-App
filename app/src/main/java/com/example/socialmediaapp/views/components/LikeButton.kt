package com.example.socialmediaapp.views.components

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.R

@Composable
fun LikeButton(like: Boolean, onLike: () -> Unit, onDislike: () -> Unit) {
    var isLiked by remember { mutableStateOf(like) }
    val context = LocalContext.current

    // Set up SoundPool with an AudioAttributes configuration for media usage
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }

    // Load the sound and get a sound ID
    val soundId = remember { soundPool.load(context, R.raw.thumbs_up, 1) }

    // Adjust the scale for the animation
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        animationSpec = tween(durationMillis = 10) // Slightly faster effect
    )

    // Choose the correct image based on the like state
    val imageRes = if (isLiked) R.drawable.liked else R.drawable.like

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Like",
        modifier = Modifier
            .size(28.dp)
            .clickable {
                if (!isLiked) {
                    // Play sound with volume control (range: 0.0 to 1.0)
                    soundPool.play(soundId, 0.5f, 0.5f, 0, 0, 1f) // Adjusted volume to 0.5f
                    onLike()
                } else {
                    onDislike()
                }
                isLiked = !isLiked
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
    )

    // Ensure SoundPool resources are released when the composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }
}
