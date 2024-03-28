package com.junghoon.movie.feature.home

import app.cash.turbine.test
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.usecase.GetHomeMovieUseCase
import com.junghoon.movie.core.domain.usecase.LikeMovieUseCase
import com.junghoon.movie.core.testing.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class HomeViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getHomeMovieUseCase: GetHomeMovieUseCase = mockk()
    private val likeMovieUseCase: LikeMovieUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    private val fakeMovie = Movie.createFake()

    @Before
    fun setup() {
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flowOf(emptySet())
    }

    @Test
    fun `홈 영화 정보를 확인한다`() = runTest {
        // given
        coEvery { getHomeMovieUseCase(MovieType.NOW_PLAYING) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.TOP_RATED) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.UPCOMING) } returns flowOf(listOf(fakeMovie))

        viewModel = HomeViewModel(getHomeMovieUseCase, likeMovieUseCase)

        // when
        viewModel.getHomeMovies()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertIs<HomeUiState.Movies>(actual)
            assertEquals(listOf(fakeMovie), actual.nowPlaying)
            assertEquals(listOf(fakeMovie), actual.topRated)
            assertEquals(listOf(fakeMovie), actual.upcoming)
        }
    }

    @Test
    fun `홈 영화 좋아요 정보를 확인한다`() = runTest {
        // given
        coEvery { getHomeMovieUseCase(MovieType.NOW_PLAYING) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.TOP_RATED) } returns flowOf(emptyList())
        coEvery { getHomeMovieUseCase(MovieType.UPCOMING) } returns flowOf(emptyList())

        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flowOf(setOf("1"))

        viewModel = HomeViewModel(getHomeMovieUseCase, likeMovieUseCase)

        // when
        viewModel.getHomeMovies()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertIs<HomeUiState.Movies>(actual)
            assertTrue(actual.nowPlaying.first().isLike)
        }
    }

    @Test
    fun `영화의 좋아요 정보를 변경할 수 있다`() = runTest {
        // given
        val id = "1"
        coEvery { getHomeMovieUseCase(MovieType.NOW_PLAYING) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.TOP_RATED) } returns flowOf(emptyList())
        coEvery { getHomeMovieUseCase(MovieType.UPCOMING) } returns flowOf(emptyList())

        val flow = MutableStateFlow(emptySet<String>())
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flow
        coEvery { likeMovieUseCase.updateMovieLike(id, true) } answers {
            flow.update { it + id }
        }

        viewModel = HomeViewModel(getHomeMovieUseCase, likeMovieUseCase)

        // when
        viewModel.getHomeMovies()
        viewModel.updateLikeMovie(1, true)

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertIs<HomeUiState.Movies>(actual)
            assertTrue(actual.nowPlaying.first().isLike)
        }
    }

    @Test
    fun `에러가 발생했을 때 빈 목록을 표시한다`() = runTest {
        // given
        coEvery { getHomeMovieUseCase(MovieType.NOW_PLAYING) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.TOP_RATED) } returns flow { throw Throwable() }
        coEvery { getHomeMovieUseCase(MovieType.UPCOMING) } returns flowOf(listOf(fakeMovie))

        viewModel = HomeViewModel(getHomeMovieUseCase, likeMovieUseCase)

        // when
        viewModel.getHomeMovies()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertIs<HomeUiState.Movies>(actual)
            assertEquals(listOf(fakeMovie), actual.nowPlaying)
            assertTrue(actual.topRated.isEmpty())
            assertEquals(listOf(fakeMovie), actual.upcoming)
        }
    }
}