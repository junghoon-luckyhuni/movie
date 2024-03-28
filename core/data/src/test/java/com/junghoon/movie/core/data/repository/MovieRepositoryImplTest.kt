package com.junghoon.movie.core.data.repository

import app.cash.turbine.test
import com.junghoon.movie.core.data.api.MovieApi
import com.junghoon.movie.core.data.api.model.MovieDetailResponse
import com.junghoon.movie.core.data.api.model.MoviesListResponse
import com.junghoon.movie.core.data.mapper.toData
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.model.Movies
import com.junghoon.movie.core.domain.repository.MovieRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first

class MovieRepositoryImplTest : StringSpec(
    {
        val movieApi: MovieApi = mockk()

        val movieRepository: MovieRepository = MovieRepositoryImpl(movieApi, FakeMoviePreferencesDataSource())


        "홈 영화 가져오기 모델 변환 테스트" {
            val expected = MoviesListResponse.createFake().results.first().toData()
            coEvery { movieApi.getHomeMovies(MovieType.NOW_PLAYING.type) } returns MoviesListResponse.createFake()

            val actual = movieRepository.getHomeMovies(MovieType.NOW_PLAYING).first()

            actual.first() shouldBe expected
        }

        "영화 상세 가져오기 모델 변환 테스트" {
            val movieId = 1
            val expected = MovieDetailResponse.createFake()
            coEvery { movieApi.getMovieDetail(movieId) } returns expected

            val actual = movieRepository.getMovieDetail(movieId).first()

            actual shouldBe expected.toData()
        }

        "Popular 영화 가져오기 모델 변환 테스트" {
            val expected = Movies.fakeMoviesPage1()
            coEvery { movieApi.getPopularMovies(1) } returns MoviesListResponse.createFake()

            val actual = movieRepository.getPopularMovies(1)

            actual shouldBe expected
        }

        "좋아요 추가 테스트" {
            movieRepository.getLikedMovieIds().test {
                awaitItem() shouldBe emptySet()

                movieRepository.updateMovieLike(id = "1", isLike = true)
                awaitItem() shouldBe setOf("1")

                movieRepository.updateMovieLike(id = "2", isLike = true)
                awaitItem() shouldBe setOf("1", "2")
            }
        }

        "좋아요 제거 테스트" {
            val bookmarkedSessionIds = listOf("1", "2", "3")
            bookmarkedSessionIds.forEach {
                movieRepository.updateMovieLike(it, true)
            }

            movieRepository.getLikedMovieIds().test {
                awaitItem() shouldBe setOf("1", "2", "3")

                // [1, 2, 3] -> [2, 3]
                movieRepository.updateMovieLike(id = "1", isLike = false)
                awaitItem() shouldBe setOf("2", "3")

                // [2, 3] -> [3]
                movieRepository.updateMovieLike(id = "2", isLike = false)
                awaitItem() shouldBe setOf("3")
            }
        }
    }
)