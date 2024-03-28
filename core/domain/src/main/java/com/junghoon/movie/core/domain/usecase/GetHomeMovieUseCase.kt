package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    operator fun invoke(movieType: MovieType): Flow<List<Movie>> {
        return movieRepository.getHomeMovies(movieType)
    }
}
