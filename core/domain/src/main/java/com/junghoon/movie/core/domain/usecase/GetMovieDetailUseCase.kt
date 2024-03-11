package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.repository.MovieRepository
import com.junghoon.movie.core.domain.model.MovieDetail
import javax.inject.Inject
import javax.inject.Singleton

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    suspend operator fun invoke(id: Int): MovieDetail {
        return movieRepository.getMovieDetail(id)
    }
}
