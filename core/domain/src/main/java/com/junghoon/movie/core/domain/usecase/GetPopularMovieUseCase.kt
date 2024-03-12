package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    private var page: Int? = null
    private val movies: MutableList<Movie> = mutableListOf()
    var canPaginate = true
        private set

    suspend operator fun invoke(pullToRefresh: Boolean = false): List<Movie> {
        if (page == null || pullToRefresh) {
            canPaginate = true
            movies.clear()
        }

        if (canPaginate) {
            val response = movieRepository.getPopularMovies(page)

            if (response.page < response.totalPages) {
                page = response.page + 1
            } else {
                canPaginate = false
            }

            val newMovies = response.results.filter { newMovie ->
                !movies.any { it.id == newMovie.id }
            }

            movies.addAll(newMovies)
        }

        return movies
    }
}
