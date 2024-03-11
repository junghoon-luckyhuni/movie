package com.junghoon.movie.detail

import app.cash.turbine.test
import com.junghoon.movie.DetailUiState
import com.junghoon.movie.DetailViewModel
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.domain.usecase.GetMovieDetailUseCase
import com.junghoon.movie.core.testing.rule.MainDispatcherRule
import com.junghoon.movie.core.ui.base.ErrorState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs


class DetailViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getMovieDetailUseCase: GetMovieDetailUseCase = mockk()
    private lateinit var viewModel: DetailViewModel

    private val fakeMovieDetail = MovieDetail.createFake()

    @Test
    fun `영화 상세 정보를 확인한다`() = runTest {
        // given
        val movieId = 1
        coEvery { getMovieDetailUseCase(movieId) } returns fakeMovieDetail
        viewModel = DetailViewModel(getMovieDetailUseCase)

        // when
        viewModel.getMovieDetail(movieId)

        // then
        viewModel.detailUiState.test {
            val actual = awaitItem()
            assertIs<DetailUiState.Detail>(actual)
        }
    }

    @Test
    fun `영화 상세 에러를 확인한다`() = runTest {
        // given
        val movieId = 1
        coEvery { getMovieDetailUseCase(movieId) } throws Throwable()
        viewModel = DetailViewModel(getMovieDetailUseCase)

        // when
        viewModel.getMovieDetail(movieId)

        // then
        viewModel.errorFlow.test {
            val actual = awaitItem()
            assertIs<ErrorState.ApiError>(actual)
        }
    }
}