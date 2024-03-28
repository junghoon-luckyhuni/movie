package com.junghoon.movie.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.core.ui.component.AnimatedLoadingView
import com.junghoon.movie.core.ui.component.LikeIcon
import com.junghoon.movie.core.ui.component.MoviePoster
import com.junghoon.movie.core.ui.component.NetworkImage
import com.junghoon.movie.core.ui.theme.MovieTheme
import com.junghoon.movie.core.ui.theme.PaleGray
import kotlinx.collections.immutable.PersistentList

@Composable
internal fun HomeRoute(
    padding: PaddingValues,
    onMovieClick: (Int) -> Unit,
    onShowErrorSnackBar: (ErrorState) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorState by viewModel.errorFlow.collectAsStateWithLifecycle()

    LaunchedEffect(errorState) {
        errorState?.let {
            onShowErrorSnackBar(it)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.getHomeMovies()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        HomeScreen(
            homeUiState = homeUiState,
            onMovieClick = onMovieClick,
            onLikeClick = { id, isLike ->
                viewModel.updateLikeMovie(id, isLike)
            }
        )
    }
}

@Composable
private fun HomeScreen(
    homeUiState: HomeUiState,
    onMovieClick: (Int) -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    when (homeUiState) {
        is HomeUiState.Empty -> Unit
        is HomeUiState.Loading -> AnimatedLoadingView(modifier = Modifier.fillMaxSize())
        is HomeUiState.Movies -> HomeSection(
            homeUiState = homeUiState,
            onMovieClick = onMovieClick,
            onLikeClick = onLikeClick
        )
    }
}

@Composable
private fun HomeSection(
    homeUiState: HomeUiState.Movies,
    onMovieClick: (Int) -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (homeUiState.nowPlaying.isNotEmpty()) {
            item {
                NowPlaySection(homeUiState.nowPlaying, onMovieClick = onMovieClick, onLikeClick = onLikeClick)
            }
        }

        if (homeUiState.topRated.isNotEmpty()) {
            item {
                TopRatedSection(homeUiState.topRated, onMovieClick = onMovieClick, onLikeClick = onLikeClick)
            }
        }

        if (homeUiState.upcoming.isNotEmpty()) {
            item {
                UpcomingSection(homeUiState.upcoming, onMovieClick = onMovieClick, onLikeClick = onLikeClick)
            }
        }
    }
}

@Composable
private fun HomeSectionTitle(modifier: Modifier = Modifier, title: String) {
    Text(modifier = modifier, text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
}

@Composable
fun TopRatedSection(movies: PersistentList<Movie>, onMovieClick: (Int) -> Unit, onLikeClick: (Int, Boolean) -> Unit) {
    Column {
        HomeSectionTitle(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = stringResource(id = R.string.top_rated_label)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(
                items = movies,
                key = { movie ->
                    movie.id
                }
            ) {
                MoviePoster(
                    modifier = Modifier
                        .fillParentMaxWidth(1f)
                        .height(180.dp),
                    poster = it.backdropPath,
                    isLike = it.isLike,
                    onMovieClick = {
                        onMovieClick(it.id)
                    },
                    onLikeClick = {
                        onLikeClick(it.id, !it.isLike)
                    }
                )
            }
        }
    }
}

@Composable
private fun NowPlaySection(
    movies: PersistentList<Movie>,
    onMovieClick: (Int) -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        HomeSectionTitle(title = stringResource(id = R.string.now_playing_label))

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                items = movies,
                key = { movie ->
                    movie.id
                }
            ) {
                MoviePoster(
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp),
                    poster = it.posterPath,
                    isLike = it.isLike,
                    onMovieClick = {
                        onMovieClick(it.id)
                    },
                    onLikeClick = {
                        onLikeClick(it.id, !it.isLike)
                    }
                )
            }
        }
    }
}

@Composable
private fun UpcomingSection(
    movies: PersistentList<Movie>,
    onMovieClick: (Int) -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        HomeSectionTitle(title = stringResource(id = R.string.upcoming_label))

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                items = movies,
                key = { movie ->
                    movie.id
                }
            ) {
                MoviePoster(
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp),
                    poster = it.posterPath,
                    isLike = it.isLike,
                    onMovieClick = {
                        onMovieClick(it.id)
                    },
                    onLikeClick = {
                        onLikeClick(it.id, !it.isLike)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeUiStatePreviewParameterProvider::class)
    homeUiState: HomeUiState,
) {
    MovieTheme {
        HomeScreen(
            homeUiState = homeUiState,
            onMovieClick = {},
            onLikeClick = { _, _ -> }
        )
    }
}

