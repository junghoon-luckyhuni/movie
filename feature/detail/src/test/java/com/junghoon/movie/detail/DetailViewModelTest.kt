package com.junghoon.movie.detail

import app.cash.turbine.test
import com.junghoon.movie.DetailUiState
import com.junghoon.movie.DetailViewModel
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.domain.usecase.GetMovieDetailUseCase
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
import kotlin.test.assertIs
import kotlin.test.assertTrue


class DetailViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getMovieDetailUseCase: GetMovieDetailUseCase = mockk()
    private val likeMovieUseCase: LikeMovieUseCase = mockk()

    private lateinit var viewModel: DetailViewModel

    private val fakeMovieDetail = MovieDetail.createFake()

    @Before
    fun setup() {
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flowOf(emptySet())
    }

    @Test
    fun `영화 상세 정보를 확인한다`() = runTest {
        // given
        val movieId = 1
        coEvery { getMovieDetailUseCase(movieId) } returns flowOf(fakeMovieDetail)
        viewModel = DetailViewModel(getMovieDetailUseCase, likeMovieUseCase)

        // when
        viewModel.getMovieDetail(movieId)

        // then
        viewModel.detailUiState.test {
            val actual = awaitItem()
            assertIs<DetailUiState.Detail>(actual)
        }
    }

    @Test
    fun `영화 상세 좋아요 정보를 확인한다`() = runTest {
        // given
        val movieId = 1
        coEvery { getMovieDetailUseCase(movieId) } returns flowOf(fakeMovieDetail)
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flowOf(setOf("1"))

        viewModel = DetailViewModel(getMovieDetailUseCase, likeMovieUseCase)

        // when
        viewModel.getMovieDetail(movieId)

        // then
        viewModel.detailUiState.test {
            val actual = awaitItem()
            assertIs<DetailUiState.Detail>(actual)
            assertTrue(actual.movieDetail.isLike)
        }
    }

    @Test
    fun `영화 상세 좋아요 정보를 변경할 수 있다`() = runTest {
        // given
        val movieId = 1
        coEvery { getMovieDetailUseCase(movieId) } returns flowOf(fakeMovieDetail)

        val flow = MutableStateFlow(emptySet<String>())
        coEvery { likeMovieUseCase.getLikedMovieIds() } returns flow
        coEvery { likeMovieUseCase.updateMovieLike(movieId.toString(), true) } answers {
            flow.update { it + movieId.toString() }
        }

        viewModel = DetailViewModel(getMovieDetailUseCase, likeMovieUseCase)

        // when
        viewModel.getMovieDetail(movieId)
        viewModel.updateLikeMovie(1, true)

        // then
        viewModel.detailUiState.test {
            val actual = awaitItem()
            assertIs<DetailUiState.Detail>(actual)
            assertTrue(actual.movieDetail.isLike)
        }
    }

    @Test
    fun `영화 상세 에러를 확인한다`() = runTest {
        // given
        val movieId = 1
        coEvery { getMovieDetailUseCase(movieId) } throws Throwable()
        viewModel = DetailViewModel(getMovieDetailUseCase, likeMovieUseCase)

        // when
        viewModel.getMovieDetail(movieId)

        // then
        viewModel.errorFlow.test {
            val actual = awaitItem()
            assertIs<ErrorState.ApiError>(actual)
        }
    }
}