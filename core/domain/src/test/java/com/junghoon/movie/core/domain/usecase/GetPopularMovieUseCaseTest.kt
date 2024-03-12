package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.model.Movies
import com.junghoon.movie.core.domain.repository.MovieRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class GetPopularMovieUseCaseTest : BehaviorSpec({
    val movieRepository: MovieRepository = mockk()
    val useCase = GetPopularMovieUseCase(movieRepository)

    Given("페이지가 null인 상태에서") {
        coEvery { movieRepository.getPopularMovies(null) } returns Movies.fakeMoviesPage1()

        When("usecase가 실행되면") {
            val actual = useCase.invoke()

            Then("첫 페이지의 데이터를 가져온다") {
                actual.size shouldBe 1
            }
        }

        When("다음 페이지 호출에 첫페이지와 동일한 중복이 있으면") {
            coEvery { movieRepository.getPopularMovies(2) } returns Movies.fakeMoviesPage2()
            val actual = useCase.invoke()

            Then("중복된 결과는 추가 되지 않는다") {
                actual.count {
                    it.id == 1
                } shouldBe 1
            }
        }

        When("3번째 페이지를 호출하면") {
            coEvery { movieRepository.getPopularMovies(3) } returns Movies.fakeMoviesPage3()
            val actual = useCase.invoke()

            Then("기존 데이터에 새로운 페이지 데이터가 추가 된다") {
                actual.size shouldBe 2
            }
        }

        When("페이징이 불가능 하면") {
            val actual = useCase.invoke()

            Then("canPaginate는 false이다") {
                useCase.canPaginate shouldBe false
            }

            Then("기존 데이터가 반환된다") {
                actual.size shouldBe 2
            }
        }

        When("pullToRefresh를 실행하면") {
            val actual = useCase.invoke(pullToRefresh = true)

            Then("첫 페이지의 데이터를 가져온다") {
                actual.size shouldBe 1
            }
        }
    }
})