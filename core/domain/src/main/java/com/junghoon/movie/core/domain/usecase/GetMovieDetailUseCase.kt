package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.repository.MovieRepository
import com.junghoon.movie.core.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    operator fun invoke(id: Int): Flow<MovieDetail> {
        return movieRepository.getMovieDetail(id)
    }
}
