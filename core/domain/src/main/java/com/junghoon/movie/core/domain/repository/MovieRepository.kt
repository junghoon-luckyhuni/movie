package com.junghoon.movie.core.domain.repository

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.model.Movies
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int?): Movies
    fun getMovieDetail(id: Int): Flow<MovieDetail>
    fun getHomeMovies(movieType: MovieType): Flow<List<Movie>>
    fun getLikedMovieIds(): Flow<Set<String>>
    suspend fun updateMovieLike(id: String, isLike: Boolean)
}