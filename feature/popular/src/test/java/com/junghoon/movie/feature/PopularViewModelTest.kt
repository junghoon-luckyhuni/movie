package com.junghoon.movie.feature

import app.cash.turbine.test
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.usecase.GetPopularMovieUseCase
import com.junghoon.movie.core.testing.rule.MainDispatcherRule
import com.junghoon.movie.core.ui.base.ErrorState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PopularViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getPopularMovieUseCase: GetPopularMovieUseCase = mockk()
    private lateinit var viewModel: PopularViewModel

    private val fakeMovie = Movie.createFake()

    @Test
    fun `Popular 영화 정보 페이징을 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase.canPaginate } returns true
        coEvery { getPopularMovieUseCase() } returns listOf(fakeMovie)

        viewModel = PopularViewModel(getPopularMovieUseCase)
     
        viewModel.loadMorePopular()

        // uiState popular 확인
        viewModel.uiState.test {
            val actual = awaitItem()
            assertEquals(false, actual.isLoading)
            assertEquals(listOf(fakeMovie), actual.popular)
        }
    }

    @Test
    fun `Popular 영화 정보 페이징 할 수 없을 때를 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase.canPaginate } returns false
        coEvery { getPopularMovieUseCase() } returns listOf(fakeMovie)

        viewModel = PopularViewModel(getPopularMovieUseCase)

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
    fun `로딩 상태를 확인한다`() = runTest {
        // given
        coEvery { getPopularMovieUseCase.canPaginate } returns true
        coEvery { getPopularMovieUseCase() } throws Throwable()

        viewModel = PopularViewModel(getPopularMovieUseCase)

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
        coEvery { getPopularMovieUseCase.canPaginate } returns true
        coEvery { getPopularMovieUseCase() } throws Throwable()

        viewModel = PopularViewModel(getPopularMovieUseCase)

        // when
        viewModel.loadMorePopular()

        // then
        viewModel.errorFlow.test {
            val actual = awaitItem()
            assertIs<ErrorState.ApiError>(actual)
        }
    }
}