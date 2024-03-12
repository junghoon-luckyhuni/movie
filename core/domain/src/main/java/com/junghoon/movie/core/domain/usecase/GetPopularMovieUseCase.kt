package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    private var page: Int? = null
    private val cachedMovies: MutableList<Movie> = mutableListOf()
    var canPaginate = true
        private set

    suspend operator fun invoke(pullToRefresh: Boolean = false): List<Movie> {
        if (pullToRefresh) {
            page = null
        }

        if (page == null) {
            canPaginate = true
            cachedMovies.clear()
        }

        if (canPaginate) {
            val response = movieRepository.getPopularMovies(page)

            if (response.page < response.totalPages) {
                page = response.page + 1
            } else {
                canPaginate = false
            }

            val newMovies = response.results.filter { newMovie ->
                !cachedMovies.any { cachedMovie ->
                    cachedMovie.id == newMovie.id
                }
            }

            cachedMovies.addAll(newMovies)

            return newMovies
        }

        return emptyList()
    }
}
