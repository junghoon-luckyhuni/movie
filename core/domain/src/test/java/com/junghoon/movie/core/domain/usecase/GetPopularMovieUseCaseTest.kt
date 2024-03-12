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

    Given("총 페이지가 3인 데이터가 있는 상태에서") {
        val expectedPage1 = Movies.fakeMoviesPage1()
        val expectedPage2 = Movies.fakeMoviesPage2()
        val expectedPage3 = Movies.fakeMoviesPage3()

        When("null인 페이지를 호출하면") {
            coEvery { movieRepository.getPopularMovies(null) } returns expectedPage1

            val actual = useCase.invoke()

            Then("canPaginate는 true이다") {
                useCase.canPaginate shouldBe true
            }

            Then("첫 페이지의 데이터를 가져온다") {
                actual shouldBe expectedPage1.results
            }
        }

        When("다음 페이지 호출 결과에 이전 페이지의 데이터와 동일한 id=1 인 중복이 있으면") {
            coEvery { movieRepository.getPopularMovies(2) } returns expectedPage2

            val actual = useCase.invoke()

            Then("canPaginate는 true이다") {
                useCase.canPaginate shouldBe true
            }

            Then("중복된 결과는 추가 되지 않는다") {
                actual.none { it.id == 1 } shouldBe true
            }
        }

        When("마지막 페이지인 3번째 페이지를 호출하면") {
            coEvery { movieRepository.getPopularMovies(3) } returns expectedPage3

            val actual = useCase.invoke()

            Then("canPaginate는 false이다") {
                useCase.canPaginate shouldBe false
            }

            Then("해당 페이지의 데이터를 가져온다") {
                actual shouldBe expectedPage3.results
            }
        }

        When("페이징이 불가능 한 상태에서 호출하면") {
            val actual = useCase.invoke()

            Then("canPaginate는 true이다") {
                useCase.canPaginate shouldBe false
            }

            Then("빈 데이터가 반환된다") {
                actual shouldBe emptyList()
            }
        }

        When("pullToRefresh를 실행하면") {
            val actual = useCase.invoke(pullToRefresh = true)

            Then("canPaginate는 true이다") {
                useCase.canPaginate shouldBe true
            }

            Then("첫 페이지의 데이터를 가져온다") {
                actual shouldBe expectedPage1.results
            }
        }
    }
})