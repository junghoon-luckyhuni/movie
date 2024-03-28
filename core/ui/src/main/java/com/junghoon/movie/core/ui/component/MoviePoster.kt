package com.junghoon.movie.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import com.junghoon.movie.core.ui.theme.PaleGray

@Composable
fun MoviePoster(
    modifier: Modifier,
    poster: String?,
    isLike: Boolean,
    onMovieClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Box {
        NetworkImage(
            imageUrl = poster,
            placeholder = ColorPainter(PaleGray),
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    onMovieClick()
                }
        )

        LikeIcon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(30.dp),
            isLike = isLike,
            onLikeClick = onLikeClick
        )
    }
}