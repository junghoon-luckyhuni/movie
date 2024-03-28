package com.junghoon.movie.feature

import app.cash.turbine.test
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.usecase.GetPopularMovieUseCase
import com.junghoon.movie.core.domain.usecase.LikeMovieUseCase
import com.junghoon.movie.core.testing.rule.MainDispatcherRule
import com.junghoon.movie.core.ui.base.ErrorState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PopularViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getPopularMovieUseCase: GetPopularMovieUseCase = mockk()
    private val likeMovieUseCase: LikeMovieUseCase = mockk()

    private lateinit var viewModel: PopularViewModel

    private val fakeMovie = Movie.createFake()

    @Before
    fun setup() {
        coEvery { getPopularMovieUseCase.canPaginate } returns true
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flowOf(emptySet())
    }

    @Test
    fun `Popular 영화 정보 페이징 데이터를 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase() } returns flowOf(listOf(fakeMovie))

        viewModel = PopularViewModel(getPopularMovieUseCase, likeMovieUseCase)

        // when
        val expectedPage2 = Movie.createFake(2)
        coEvery { getPopularMovieUseCase() } returns flowOf(listOf(expectedPage2))
        viewModel.loadMorePopular()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertEquals(false, actual.isLoading)
            assertEquals(true, actual.popular.any { it.id == fakeMovie.id })
            assertEquals(true, actual.popular.any { it.id == expectedPage2.id })
        }
    }

    @Test
    fun `Popular 영화 정보 페이징 할 수 없을 때를 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase.canPaginate } returns false
        coEvery { getPopularMovieUseCase() } returns flowOf(listOf(fakeMovie))

        viewModel = PopularViewModel(getPopularMovieUseCase, likeMovieUseCase)

        // when
        viewModel.loadMorePopular()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertEquals(false, actual.isLoading)
            assertEquals(listOf(fakeMovie), actual.popular)
        }
    }

    @Test
    fun `Popular 영화 좋아요 정보를 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase() } returns flowOf(listOf(fakeMovie))
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flowOf(setOf("1"))

        viewModel = PopularViewModel(getPopularMovieUseCase, likeMovieUseCase)

        // when
        viewModel.loadMorePopular()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertTrue(actual.popular.first().isLike)
        }
    }

    @Test
    fun `Popular 영화 좋아요 정보를 변경할 수 있다`() = runTest {
        // given
        val movieId = 1
        coEvery { getPopularMovieUseCase() } returns flowOf(listOf(fakeMovie))

        val flow = MutableStateFlow(emptySet<String>())
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flow
        coEvery { likeMovieUseCase.updateMovieLike(movieId.toString(), true) } answers {
            flow.update { it + movieId.toString() }
        }

        viewModel = PopularViewModel(getPopularMovieUseCase, likeMovieUseCase)

        // when
        viewModel.loadMorePopular()
        viewModel.updateLikeMovie(1, true)

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertEquals(false, actual.isLoading)
            assertTrue(actual.popular.first().isLike)
        }
    }

    @Test
    fun `로딩 상태를 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase() } throws Throwable()

        viewModel = PopularViewModel(getPopularMovieUseCase, likeMovieUseCase)

        // when
        viewModel.loadMorePopular()

        // then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertEquals(true, actual.isLoading)
        }
    }

    @Test
    fun `에러가 발생했을 때 빈 목록을 표시한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase() } throws Throwable()

        viewModel = PopularViewModel(getPopularMovieUseCase, likeMovieUseCase)

        // when
        viewModel.loadMorePopular()

        // then
        viewModel.errorFlow.test {
            val actual = awaitItem()
            assertIs<ErrorState.ApiError>(actual)
        }
    }
}