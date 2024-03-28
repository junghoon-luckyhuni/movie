package com.junghoon.movie.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.junghoon.movie.core.ui.R
import com.junghoon.movie.core.ui.theme.Red03

@Composable
fun LikeIcon(
    modifier: Modifier = Modifier,
    isLike: Boolean,
    onLikeClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onLikeClick()
        }
    ) {
        Icon(
            painter = painterResource(id = if (isLike) R.drawable.ic_like_on else R.drawable.ic_like_off),
            contentDescription = null,
            tint = Red03
        )
    }
}
