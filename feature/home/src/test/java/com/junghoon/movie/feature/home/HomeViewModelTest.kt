package com.junghoon.movie.feature.home

import app.cash.turbine.test
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.usecase.GetHomeMovieUseCase
import com.junghoon.movie.core.testing.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class HomeViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getHomeMovieUseCase: GetHomeMovieUseCase = mockk()
    private lateinit var viewModel: HomeViewModel

    private val fakeMovie = Movie.createFake()

    @Test
    fun `홈 영화 정보를 확인한다`() = runTest {
        // given
        coEvery { getHomeMovieUseCase(MovieType.NOW_PLAYING) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.TOP_RATED) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.UPCOMING) } returns flowOf(listOf(fakeMovie))

        viewModel = HomeViewModel(getHomeMovieUseCase)

        // when
        viewModel.getHomeMovies()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertIs<HomeUiState.Movies>(actual)
        }
    }

    @Test
    fun `에러가 발생했을 때 빈 목록을 표시한다`() = runTest {
        // given
        coEvery { getHomeMovieUseCase(MovieType.NOW_PLAYING) } returns flowOf(listOf(fakeMovie))
        coEvery { getHomeMovieUseCase(MovieType.TOP_RATED) } returns flow { throw Throwable() }
        coEvery { getHomeMovieUseCase(MovieType.UPCOMING) } returns flowOf(listOf(fakeMovie))

        viewModel = HomeViewModel(getHomeMovieUseCase)

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