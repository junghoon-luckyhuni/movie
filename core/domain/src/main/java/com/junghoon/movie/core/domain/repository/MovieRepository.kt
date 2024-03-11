package com.junghoon.movie.core.domain.repository

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.model.Movies
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int?): Movies
    suspend fun getMovieDetail(id: Int): MovieDetail
    suspend fun getHomeMovies(movieType: MovieType): Flow<List<Movie>>
}