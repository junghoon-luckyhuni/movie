package com.junghoon.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.core.ui.component.AnimatedLoadingView
import com.junghoon.movie.core.ui.component.LikeIcon
import com.junghoon.movie.core.ui.component.NetworkImage
import com.junghoon.movie.core.ui.component.RatingBar
import com.junghoon.movie.core.ui.theme.PaleGray
import com.junghoon.movie.feature.detail.R

@Composable
internal fun MovieDetailRoute(
    movieId: Int,
    onBackClick: () -> Unit,
    onShowErrorSnackBar: (ErrorState) -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val movieDetailUiState by viewModel.detailUiState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorFlow.collectAsStateWithLifecycle()

    LaunchedEffect(errorState) {
        errorState?.let {
            onShowErrorSnackBar(it)
        }
    }

    LaunchedEffect(key1 = movieId) {
        viewModel.getMovieDetail(movieId)
    }

    MovieDetailScreen(
        uiState = movieDetailUiState,
        onBackClick = onBackClick,
        onLikeClick = { id, isLike ->
            viewModel.updateLikeMovie(id, isLike)
        }
    )
}

@Composable
internal fun MovieDetailScreen(uiState: DetailUiState, onBackClick: () -> Unit, onLikeClick: (Int, Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        MovieDetailTopBarSection(uiState = uiState, onBackClick = onBackClick, onLikeClick = onLikeClick)

        when (uiState) {
            is DetailUiState.Loading -> {
                AnimatedLoadingView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is DetailUiState.Detail -> MovieDetailSection(uiState = uiState)
        }
    }
}

@Composable
private fun MovieDetailTopBarSection(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
            )
        }

        if (uiState is DetailUiState.Detail) {
            uiState.movieDetail.let { movieDetail ->
                LikeIcon(
                    isLike = movieDetail.isLike,
                    onLikeClick = {
                        onLikeClick(movieDetail.id, !movieDetail.isLike)
                    }
                )
            }
        }
    }
}

@Composable
private fun MovieDetailSection(uiState: DetailUiState.Detail) {
    Column {
        val movieDetail = uiState.movieDetail

        NetworkImage(
            imageUrl = movieDetail.backdropPath,
            placeholder = ColorPainter(PaleGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            NetworkImage(
                imageUrl = movieDetail.posterPath,
                placeholder = ColorPainter(PaleGray),
                modifier = Modifier
                    .width(150.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(20.dp))

            InfoSection(movieDetail = movieDetail)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OverviewSection(modifier = Modifier.padding(horizontal = 20.dp), movieDetail = movieDetail)

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun InfoSection(movieDetail: MovieDetail) {
    Column {
        Text(
            text = movieDetail.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(
                modifier = Modifier,
                starsModifier = Modifier.size(18.dp),
                rating = movieDetail.rating.div(2)
            )

            Text(
                modifier = Modifier.padding(
                    horizontal = 4.dp
                ),
                text = movieDetail.rating.toString().take(3),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "\"${movieDetail.tagline}\"",
            fontSize = 17.sp,
            fontStyle = FontStyle.Italic,
            maxLines = 3
        )
    }
}

@Composable
fun OverviewSection(
    modifier: Modifier,
    movieDetail: MovieDetail
) {
    Column(modifier = modifier) {

        Text(
            text = stringResource(R.string.overview),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}