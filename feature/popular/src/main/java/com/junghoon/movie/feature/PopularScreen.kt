package com.junghoon.movie.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.core.ui.component.AnimatedLoadingView
import com.junghoon.movie.core.ui.component.MoviePoster
import com.junghoon.movie.core.ui.theme.MovieTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun PopularRoute(
    padding: PaddingValues,
    onShowErrorSnackBar: (ErrorState) -> Unit,
    onMovieClick: (Int) -> Unit,
    viewModel: PopularViewModel = hiltViewModel()
) {
    val errorState by viewModel.errorFlow.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(errorState) {
        errorState?.let {
            onShowErrorSnackBar(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        PopularScreen(
            popular = uiState.popular,
            onMovieClick = onMovieClick,
            onLoadMore = {
                viewModel.loadMorePopular()
            },
            onLikeClick = { id, isLike ->
                viewModel.updateLikeMovie(id, isLike)
            }
        )

        if (uiState.isLoading) {
            AnimatedLoadingView(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun PopularScreen(
    popular: PersistentList<Movie>,
    onMovieClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    val gridState = rememberLazyGridState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            (gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (gridState.layoutInfo.totalItemsCount - 9)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(
            items = popular,
            key = { movie ->
                movie.id
            }
        ) { movie ->
            MovieView(
                movie = movie,
                onMovieClick = {
                    onMovieClick(movie.id)
                },
                onLikeClick = onLikeClick
            )
        }
    }
}

@Composable
private fun MovieView(movie: Movie, onMovieClick: () -> Unit, onLikeClick: (Int, Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MoviePoster(
            modifier = Modifier
                .width(150.dp)
                .height(200.dp),
            poster = movie.posterPath,
            isLike = movie.isLike,
            onMovieClick = onMovieClick,
            onLikeClick = {
                onLikeClick(movie.id, !movie.isLike)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = movie.title,
            style = MovieTheme.typography.bodySmallR,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview
@Composable
private fun PopularScreenPreview() {
    MovieTheme {
        val popular = mutableListOf<Movie>()
        repeat(8) { popular.add(Movie.createFake(id = it)) }

        PopularScreen(
            popular = popular.toPersistentList(),
            onMovieClick = { },
            onLoadMore = {},
            onLikeClick = { _, _ -> }
        )
    }
}
