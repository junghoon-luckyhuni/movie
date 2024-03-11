package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.repository.MovieRepository
import com.junghoon.movie.core.domain.model.Movies
import javax.inject.Inject
import javax.inject.Singleton

class GetPopularMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {
    private var page: Int? = null
    private val movies: MutableList<Movie> = mutableListOf()
    var canPaginate = true
        private set

    suspend operator fun invoke(): List<Movie> {
        if (page == null) {
            movies.clear()
        }

        val response = movieRepository.getPopularMovies(page)

        page = if (response.page < response.totalPages) {
            response.page + 1
        } else {
            null
        }

        canPaginate = page != null

        val newMovies = response.results.filter { newMovie ->
            !movies.any { it.id == newMovie.id }
        }

        movies.addAll(newMovies)

        return movies
    }
}
