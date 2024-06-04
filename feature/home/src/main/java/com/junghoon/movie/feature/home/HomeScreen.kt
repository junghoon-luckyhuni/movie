package com.junghoon.movie.feature.home

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.core.ui.component.AnimatedLoadingView
import com.junghoon.movie.core.ui.component.MoviePoster
import com.junghoon.movie.core.ui.theme.MovieTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.absoluteValue
import kotlin.math.min

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

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

@Composable
private fun NowPlaySection(
    movies: PersistentList<Movie>,
    onMovieClick: (Int) -> Unit,
    onLikeClick: (Int, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        HomeSectionTitle(title = stringResource(id = R.string.now_playing_label))

        Spacer(modifier = Modifier.height(10.dp))

        AutoScrollNowPlaySection(movies = movies, onMovieClick = onMovieClick, onLikeClick = onLikeClick)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AutoScrollNowPlaySection(movies: PersistentList<Movie>, onMovieClick: (Int) -> Unit, onLikeClick: (Int, Boolean) -> Unit) {
    val deg = 105f

    val pagerState = rememberPagerState()

    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            runCatching {
                delay(2000)
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage.inc()) % Integer.MAX_VALUE,
                    animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    val scale by remember {
        derivedStateOf {
            1f - (pagerState.currentPageOffsetFraction.absoluteValue) * 0.3f
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val offsetFromStart = pagerState.offsetForPage(0).absoluteValue
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .offset { IntOffset(0, 160.dp.roundToPx()) }
                .scale(scaleX = 0.6f, scaleY = 0.24f)
                .scale(scale)
                .rotate(offsetFromStart * 90f)
                .blur(
                    radius = 110.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded,
                )
                .background(Color.White.copy(alpha = 0.5f))
        )

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            pageCount = Integer.MAX_VALUE,
            state = pagerState
        ) { page ->
            Box(
                modifier = Modifier
                    .aspectRatio(0.7f)
                    .graphicsLayer {
                        val pageOffset = pagerState.offsetForPage(page)
                        val offScreenRight = pageOffset < 0f

                        val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
                        rotationY = min(interpolated * if (offScreenRight) deg else -deg, 90f)

                        transformOrigin = TransformOrigin(
                            pivotFractionX = if (offScreenRight) 0f else 1f,
                            pivotFractionY = 0.5f
                        )
                    }
                    .drawWithContent {
                        this.drawContent()

                        drawRoundRect(
                            color = Color.Black.copy((pagerState.offsetForPage(page).absoluteValue * 0.7f)),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    }
            ) {
                val movie = movies[page % movies.size]

                MoviePoster(
                    modifier = Modifier
                        .fillMaxSize(),
                    poster = movie.posterPath,
                    isLike = movie.isLike,
                    onMovieClick = {
                        onMovieClick(movie.id)
                    },
                    onLikeClick = {
                        onLikeClick(movie.id, !movie.isLike)
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

