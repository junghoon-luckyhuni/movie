package com.junghoon.movie.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedLoadingView(modifier: Modifier = Modifier) {
    val images = listOf(
        R.drawable.ic_loading_01,
        R.drawable.ic_loading_02,
        R.drawable.ic_loading_03,
        R.drawable.ic_loading_04,
        R.drawable.ic_loading_05
    )

    var currentFrame by remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        delay(150)
        currentFrame = 0

        while (true) {
            delay(100)
            currentFrame = (currentFrame + 1) % images.size
        }
    }

    if (currentFrame != -1) {
        Box(modifier = modifier) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.Center)
                    .background(color = Color.White, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = images[currentFrame]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}